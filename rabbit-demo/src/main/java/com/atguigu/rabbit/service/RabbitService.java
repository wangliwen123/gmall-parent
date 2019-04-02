package com.atguigu.rabbit.service;

import com.atguigu.rabbit.bean.Order;
import com.atguigu.rabbit.bean.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitService {


    @RabbitListener(queues = "deadQueue")
    public void orderCancel(Order order,Channel channel,Message message) throws IOException {
        System.out.println("收到了超时订单..."+order);

        if(order.getStatus().equals("PAYED")){
            System.out.println("此订单已经支付，，，不用操作");
        }else {
            System.out.println("此订单将被关闭....");
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 开启手动确认回复模式
     * 1）、消息丢失
     *      1：手动回复ack
     *
     *
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "atguigu.emps")
    public void vc(Message message,Channel channel) throws IOException {
        System.out.println("收到消息..."+message.getMessageProperties().getDeliveryTag());
        //只要某个消息，没回复，这个消息是unacked状态，而且不会继续发过来
        if (message.getMessageProperties().getDeliveryTag()%2==0){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }

    }

    /**
     * 方法上可以有三种参数
     * 1）、Message 收到的消息封装到message
     * 2）、直接写将消息封装成什么类型
     * 3）、可以写Channel
     */
    //@RabbitListener(queues = "atguigu")
    public void consumer(User user, Channel channel,Message message) throws IOException {

        //System.out.println("【收到了消息】..."+message.getBody());



        //channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);


        //告诉RabbitMQ我收到消息
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        try{
            if(user.getAge()%2==0){
                System.out.println("【1号服务器...收到了消息，并处理完成】..."+user);
            }
            throw  new RuntimeException();
        }catch (Exception e){
            //拒绝了消息，并重新入队  ack确认收到   nack：不给回复确认收到 == reject
            System.out.println("一号服务器故障，消息拒绝");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }

    }

    /**
     * 下单完成。订单消息放到mq中
     * 库存系统订阅mq；
     *      1 ---
     *      2 == order ===减库存。basicNack  basicAck
     *      3 ---
     * @param user
     * @param channel
     * @param message
     * @throws IOException
     */
    //@RabbitListener(queues = "atguigu")
    public  void consume(User user,Channel channel,Message message) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        System.out.println("【2号服务器...收到了消息】..."+user);
        try{
            if(user.getAge()%2!=0){
                System.out.println("【2号服务器...收到了消息，并处理完成】..."+user);
            }
            throw  new RuntimeException();
        }catch (Exception e){
            //拒绝了消息，并重新入队  ack确认收到   nack：不给回复确认收到 == reject
            System.out.println("2号服务器故障，消息拒绝");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }


        // channel.basicNack();
        // channel.basicReject();
    }


}
