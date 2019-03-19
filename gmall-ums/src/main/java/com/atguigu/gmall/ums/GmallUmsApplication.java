package com.atguigu.gmall.ums;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@MapperScan("com.atguigu.gmall.ums.mapper")
@SpringBootApplication
public class GmallUmsApplication {


    /**
     * 1、逆向工程生成完成以后
     *    1）、将逆向工程这个模块的 bean -interface复制到 api层
     *    2）、将生成的service.impl下的所有复制到业务层
     *          自己创建service,复制 impl以及下面所有
     *
     * 整合MyBatis-Plus
     * 1）、配置数据源
     * 2）、配置MapperScan
     * @param args
     */
    public static void main(String[] args) {


        SpringApplication.run(GmallUmsApplication.class, args);
    }

}
