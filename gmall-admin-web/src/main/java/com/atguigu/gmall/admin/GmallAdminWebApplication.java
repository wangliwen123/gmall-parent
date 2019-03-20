package com.atguigu.gmall.admin;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 1、dubbo项目默认启动的时候，dubbo会检查自己远程服务是否在线。不在就报错；
 * 2、我们可以关闭dubbo的启动检查
 *
 *
 * 分步式 RestAPI与前端对接的问题
 * 1）、跨域
 * 2）、mp调用的问题
 *      1）、每个Service都标两个注解
 *      2）、web都是refrence
 *      3）、注意不要用复杂类的mp自动生成的方法
 */
@EnableDubbo
@SpringBootApplication
public class GmallAdminWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallAdminWebApplication.class, args);
    }

}
