package com.atguigu.gmall.cart.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmallRedissonConfig {

    @Bean
    public RedissonClient redissonClient(@Value("${spring.redis.host}") String host){

        Config config = new Config();

        config.useSingleServer().setAddress("redis://"+host+":6379");



        return  Redisson.create(config);
    }
}
