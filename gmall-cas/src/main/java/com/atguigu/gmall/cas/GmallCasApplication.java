package com.atguigu.gmall.cas;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 集成社交登陆
 */
@EnableDubbo
@SpringBootApplication
public class GmallCasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallCasApplication.class, args);
    }

}
