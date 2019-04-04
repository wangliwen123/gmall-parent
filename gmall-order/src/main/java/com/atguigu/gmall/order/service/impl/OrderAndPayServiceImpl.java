package com.atguigu.gmall.order.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.order.config.AlipayConfig;
import com.atguigu.gmall.order.mapper.OrderItemMapper;
import com.atguigu.gmall.order.mapper.OrderMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.to.OrderStatusEnume;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.ums.service.MemberService;
import com.atguigu.gmall.vo.OrderResponseVo;
import com.atguigu.gmall.vo.OrderSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Component
public class OrderAndPayServiceImpl implements OrderAndPayService {


    @Reference
    MemberService memberService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Reference
    CartService cartService;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public List<MemberReceiveAddress> getUserRecieveAddress(String token) {

        String s = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(s, Member.class);
        if(member!=null){
            return memberService.getUserAddress(member.getId());
        }
        return null;
    }

    @Override
    public String geiwoTradeToken() {
        String gmallusertoken = (String) RpcContext.getContext().getAttachment("gmallusertoken");
        String replace = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisCacheConstant.TRADE_TOKEN+gmallusertoken,replace,RedisCacheConstant.TRADE_TOKEN_TIME, TimeUnit.MINUTES);
        return replace;
    }

    @Transactional
    @Override
    public OrderResponseVo createOrder(OrderSubmitVo orderSubmitVo) {
        OrderResponseVo responseVo = new OrderResponseVo();
        //1、验证防止重复
        String tradeToken = orderSubmitVo.getTradeToken();
        //分布式锁 RedisCacheConstant.TRADE_TOKEN+userToken(userId)

        //对比防重删令牌
        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        Long eval = (Long) jedis.eval(script, Collections.singletonList(
                RedisCacheConstant.TRADE_TOKEN + orderSubmitVo.getToken()),
                Collections.singletonList(tradeToken));
        if(eval == 1){
            //令牌验证通过并且删除；总单信息；
            Order order = new Order();

            //1、查出用户信息
            String s = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + orderSubmitVo.getToken());
            Member member = JSON.parseObject(s, Member.class);
            order.setMemberId(member.getId());
            order.setMemberUsername(member.getUsername());
            
            //2、查出用户的收货地址
            Long addressId = orderSubmitVo.getAddressId();
            MemberReceiveAddress address = memberService.getUserAddressByAddressId(addressId);
            order.setReceiverCity(address.getCity());
            order.setReceiverDetailAddress(address.getDetailAddress());
            order.setReceiverName(address.getName());
            order.setReceiverPhone(address.getPhoneNumber());
            order.setReceiverProvince(address.getProvince());
            order.setReceiverRegion(address.getRegion());

            //3、计算订单总额信息；
            List<CartItem> cartItems = cartService.cartItemsForJieSuan(orderSubmitVo.getToken());
            Cart cart = new Cart();
            cart.setItems(cartItems);

            BigDecimal totalPrice = cart.getTotalPrice();
            order.setTotalAmount(totalPrice);

            //4、订单状态是未支付
            order.setStatus(OrderStatusEnume.UNPAY.getCode());

            //5、订单号  1000万？ 000000001
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String date_prefix = sf.format(new Date());
            Long countId = redisTemplate.opsForValue().increment("orderCountId");

            NumberFormat numberFormat = NumberFormat.getInstance();

            numberFormat.setMaximumIntegerDigits(9);
            numberFormat.setMinimumIntegerDigits(9);

            final String number_suffix = numberFormat.format(countId);

            //订单号
            String orderSn = date_prefix+number_suffix.replace(",","");
            order.setOrderSn(orderSn);

            //先保存订单
            orderMapper.insert(order);

            //2）、收集订单项信息
           List<OrderItem> orderItems = new ArrayList<>();
            cartItems.forEach((cartItem)->{
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(cartItem,orderItem);
                    //productQuantity
                orderItem.setProductQuantity(cartItem.getNum());
                    //realAmount
                orderItem.setRealAmount(cartItem.getNewPrice());
                orderItem.setOrderSn(orderSn);
                orderItem.setOrderId(order.getId());
                orderItems.add(orderItem);
                orderItemMapper.insert(orderItem);
            });
            //3）、以上收集了订单信息和订单项信息,批量保存所有订单想
           // orderItemMapper.insertBatch(orderItems);
            //4）、将订单创建完成这个消息发布出去；
            OrderMQTo orderMQTo = new OrderMQTo(order, orderItems);
            rabbitTemplate.convertAndSend("orderFanoutExchange","order",orderMQTo);


            //5、响应
            //订单提交成功，请尽快付款！订单号：85412418005
            return new OrderResponseVo("订单创建成功",0,
                    orderSn,order.getTotalAmount().toString(),
                    "订单提交成功，请尽快付款!订单号："+orderSn,
                    orderItems.get(0).getProductName());

        }else {
            throw  new RuntimeException("令牌过期...请重新结算");
        }

    }


    /**
     *
     * @param out_trade_no   商户在支付宝保存的订单号
     * @param total_amount   订单总金额
     * @param subject        订单表题
     * @param body           订单的body
     * @return
     */
    public String payMyOrder(String out_trade_no, String total_amount, String subject, String body) {
        //0、验价
        //一不要让MySQL隐式转换   price="12.98"
        BigDecimal bigDecimal = new BigDecimal(total_amount);

        Order orderSn = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_sn", out_trade_no));
        if(!orderSn.getTotalAmount().equals(bigDecimal)){
            //验证成功...
            throw  new RuntimeException("前端非法提交请求");
        }

        // 1、创建支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        // 2、创建一次支付请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        // 付款金额，必填
        // 订单名称，必填
        // 商品描述，可空

        // 3、构造支付请求数据
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
                + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = "";
        try {
            // 4、请求
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;// 支付跳转页的代码

    }

    @Override
    public void updateOrderStatus(String out_trade_no, OrderStatusEnume finished) {
        Order order = new Order();
        order.setStatus(finished.getCode());
        orderMapper.update(order,new UpdateWrapper<Order>().eq("order_sn",out_trade_no));



    }

    @Override
    public Order getOrderByOrderSn(String orderSn) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_sn", orderSn));
        return order;
    }
}
