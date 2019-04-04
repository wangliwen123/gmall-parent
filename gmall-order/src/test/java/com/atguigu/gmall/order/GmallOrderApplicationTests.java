package com.atguigu.gmall.order;

import com.atguigu.gmall.constant.RedisCacheConstant;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallOrderApplicationTests {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        NumberFormat numberFormat = NumberFormat.getInstance();

        numberFormat.setMaximumIntegerDigits(9);
        numberFormat.setMinimumIntegerDigits(9);

        final String number_suffix = numberFormat.format(1);
        System.out.println(number_suffix.replace(",",""));
    }

    @Test
    public void limiter(){
        redisTemplate.opsForValue().set(RedisCacheConstant.SEC_KILL+145+"","100");

    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(10);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(()->{
                double acquire = rateLimiter.acquire();
                System.out.println("6666"+ finalI+"==>"+acquire);
            },""+i).start();
        }
    }

}
