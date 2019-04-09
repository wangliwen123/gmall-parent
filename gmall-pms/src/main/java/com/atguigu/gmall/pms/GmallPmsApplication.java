package com.atguigu.gmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import io.lettuce.core.RedisClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 1、开启事务@EnableTransactionManagement
 * 2、合理使用Required，Require_New
 * 3、解决service自己掉自己方法没有事务控制的问题
 *  （原因：xxxService.xxx()，xxx(),yyy()，拿不到自己的代理对象，解决：拿到自己的代理对象）
 *      1、让别的service调；
 *      【2】、自己调自己
 *             1）、引入spring-aop（高级aop场景，aspectj）
 *             2）、@EnableTransactionManagement(proxyTargetClass = true)
 *             3）、@EnableAspectJAutoProxy(exposeProxy = true) //暴露出这些类的代理对象
 *                  然后再用ProductServiceImpl psProxy = (ProductServiceImpl) AopContext.currentProxy();
 *                  再去调方法
 *
 *
 */
//开启事务
//@EnableHystrix 随便表一个
////@EnableCircuitBreaker
@EnableHystrix
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDubbo
@EnableRabbit
@MapperScan("com.atguigu.gmall.pms.mapper")
@SpringBootApplication
public class GmallPmsApplication {



    public static void main(String[] args) {
        SpringApplication.run(GmallPmsApplication.class, args);

    }

}
