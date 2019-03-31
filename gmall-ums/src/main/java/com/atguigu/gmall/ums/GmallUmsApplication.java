package com.atguigu.gmall.ums;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 1）、引入mp和dubbo依赖‘
 * 2）、application.properties中配置dubbo暴露服务
 * 3）、Main中配置 mp的包扫描和@EnableDubbo 即可
 * 4）、@Service dubbo家的暴露服务
 *      为每一个服务标两个
 *         @Service(dubbo的)  为了让了别人refrence
 *          @Component 为了让自己模块Autowired
 *          public class AdminPermissionRelationServiceImpl
 */
@EnableTransactionManagement
@EnableDubbo
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
