package com.atguigu.gmall.ums.config;


import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.util.DataSourceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;


/**
 * 1、使用sharding-jdbc创建一个主从数据源
 * 2、配置mybatis-plus使用这个数据源
 * @MapperScan
 */
@Configuration
public class GmallShardingJdbcConfig {

    @Bean
    public DataSource dataSource() throws IOException, SQLException {

        //使用sharding-jdbc创建出具有主从库的数据源
        DataSource dataSource = MasterSlaveDataSourceFactory
                .createDataSource(ResourceUtils.getFile("classpath:sharding.yml"));

        return  dataSource;
    }
}
