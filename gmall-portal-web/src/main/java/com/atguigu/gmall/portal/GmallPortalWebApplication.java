package com.atguigu.gmall.portal;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDubbo
@EnableSwagger2
@SpringBootApplication
public class GmallPortalWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPortalWebApplication.class, args);
    }

}
