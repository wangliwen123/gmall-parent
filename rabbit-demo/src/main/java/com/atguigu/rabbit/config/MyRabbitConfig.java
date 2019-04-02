package com.atguigu.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyRabbitConfig {


    /**
     * 1、创建一个普通的交换机
     * @return
     */
    @Bean
    public Exchange delayExchange(){
        return new DirectExchange("delayExchange",true,false);
    }

    @Bean
    public Queue delayQueue(){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","deadExchange");
        arguments.put("x-dead-letter-routing-key","dead");
        arguments.put("x-message-ttl",1000*60);
        Queue queue = new Queue("delayQueue",true,false,false,arguments);
        return queue;
    }


    @Bean
    public Binding delayBinding(){
        Binding binding = new Binding("delayQueue",
                Binding.DestinationType.QUEUE,
                "delayExchange",
                "order",
                null);
        return binding;
    }



    //-------------死信交换机与存储死信的队列绑定起来----------
    @Bean
    public Exchange deadExchange(){
        return new DirectExchange("deadExchange",true,false);
    }

    @Bean
    public Queue deadQueue(){
        Queue queue = new Queue("deadQueue",true,false,false);
        return queue;
    }

    @Bean
    public Binding deadBinding(){
        Binding binding = new Binding("deadQueue",
                Binding.DestinationType.QUEUE,
                "deadExchange",
                "dead",
                null);
        return binding;
    }
//-------------------------------------------

    /**
     * 全系统使用json进行转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 只需要把 Queue、Exchange、Binding放在容器即可
     *      MQ中没有就创建，有就用mq中的映射上
     * @return
     */

    @Bean
    public Queue queue(){
        return new Queue("hehehe",true,false,false);
    }

    @Bean
    public Exchange exchange(){
        return new DirectExchange("direct-exchange-777",true,false);
    }

    @Bean
    public Binding binding(){
        return new Binding("hehehe", Binding.DestinationType.QUEUE,
                "direct-exchange-777","hehehe",null);
    }
}
