package com.atguigu.gmall.order;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 1、开启定时任务功能
 */
@EnableScheduling
@MapperScan("com.atguigu.gmall.order.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)  //要导入aop模块
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDubbo
@SpringBootApplication
public class GmallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallOrderApplication.class, args);



    }




}
