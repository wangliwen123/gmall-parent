package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.order.mapper.OrderMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.to.OrderStatusEnume;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderCloseService {

    
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 死信关单
     * @param order
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = "deadOrderQueue")
    public void closeOrder(OrderMQTo order, Channel channel, Message message) throws IOException {
        //1、获取到order的状态
        try {
            Order order1 = orderMapper.selectById(order.getOrder().getId());
            if(order1.getStatus()== OrderStatusEnume.UNPAY.getCode()){
                Order saveOrder = new Order();
                saveOrder.setId(order1.getId());
                saveOrder.setStatus(OrderStatusEnume.CLOSED.getCode());
                orderMapper.updateById(saveOrder);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                //关单完成，将关闭的订单解锁库存
                rabbitTemplate.convertAndSend("orderDeadExchange","release.stock",order);
            }
        }catch (Exception e){
            //重新入队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            //channel.basicNack();
        }
    }


}
