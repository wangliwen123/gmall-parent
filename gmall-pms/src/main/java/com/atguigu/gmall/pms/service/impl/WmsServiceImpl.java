package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.mapper.SkuStockMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.to.OrderStatusEnume;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 库存的最终方案：
 * 1）、缓存为王；
 *      1）、缓存不一致；
 *          1）、缓存的使用模式之（Cache-Aside）【1、双写  2、写数据库，清缓存】
 *      2）、高并发读写特别多，保持实时同步；
 *          ABA；
 *          改库存：
 *              1）、1
 *              2）、2
 *              3）、1
 *          1）、读写锁（改动特别多）；
 *              实时数据，加读写写锁。全是读多；
 *
 *
 *
 */
@Slf4j
@Service
public class WmsServiceImpl {

    @Autowired
    SkuStockMapper skuStockMapper;

    @Reference
    OrderAndPayService orderAndPayService;


    /**
     * 后台锁库存
     * @param orderMQTo
     * @param channel
     * @param message
     */
    @RabbitListener(queues = "stockOrderQueue")
    public void lockStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {
        try {


            log.debug("订单开始锁库存："+orderMQTo.getOrder().getOrderSn());
            List<OrderItem> items = orderMQTo.getItems();
            items.forEach((item)->{
                Long skuId = item.getProductSkuId();
                Integer quantity = item.getProductQuantity();

                //并发锁库存；库存可以一直锁；预警系统；

                //锁库存
                //1）、前端页面会查询商品的详情；查一次库存（前端控制没有购买链接）
                //2）、前端去结算，确认结算信息；继续查库存（此时没库存，不给下订单）
                //3）、下订单的时候只去发送锁库存信息。超的库存
                //  （超卖【1）、分布式锁，锁住某个商品
                //  2）、运维，允许超卖。进入其他运营流程】）

                //直接去锁库存
                SkuStock skuStock = new SkuStock();
                skuStock.setId(skuId);
                skuStockMapper.updateStock(skuId,quantity);
                log.debug("Sku："+skuId+"：库存锁定成功..."+quantity);
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                } catch (IOException e) {
                    //
                }
            });

        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }


    /**
     *
     * 业务撤销与回滚；（undo_log）；order plus 1
     *      定时扫描；（日志记录；undo_log；bin_log）
     *      1 closed 12
     *
     * 消息队列；
     *
     * @param orderMQTo
     * @param channel
     * @param message
     */
    @RabbitListener(queues = "releaseStockQueue")
    public void releaseStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {


        try {
            //1、解锁库存
            String orderSn = orderMQTo.getOrder().getOrderSn();

            //2、获取订单的最新状态
            Order order = orderAndPayService.getOrderByOrderSn(orderSn);
            if(order.getStatus()== OrderStatusEnume.CLOSED.getCode()){
                //释放之前的库存
                List<OrderItem> items = orderMQTo.getItems();
                items.forEach((item)->{
                    Long productSkuId = item.getProductSkuId();
                    Integer productQuantity = item.getProductQuantity();
                    skuStockMapper.releaseStock(productSkuId,productQuantity);
                });
            }

            //消息的重复消费，忘了手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }


}
