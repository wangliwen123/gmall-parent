package com.atguigu.gmall.sms.config;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 1、使用sharding-jdbc创建一个主从数据源
 * 2、配置mybatis-plus使用这个数据源
 *
 * @author: loy
 * @create: 2019-03-19 20:36:58
 */
@Configuration
public class GmallShardingJdbcConfig {

    /**
     * 我们自定义的数据源会替换调系统给我们提供的数据源
     *
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        return MasterSlaveDataSourceFactory.createDataSource(ResourceUtils.getFile("classpath:sharding.yml"));
    }

}
