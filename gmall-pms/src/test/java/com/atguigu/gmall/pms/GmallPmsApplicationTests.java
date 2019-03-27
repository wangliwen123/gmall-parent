package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.to.PmsProductCategoryWithChildrenItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPmsApplicationTests {

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    JedisConnectionFactory connectionFactory;

    @Test
    public void contextLoads() {

        List<PmsProductCategoryWithChildrenItem> items = productCategoryMapper.listWithChildren(0);
        System.out.println(items);
    }

    @Test
    public void testJedis() {
        System.out.println(connectionFactory);
        Jedis jedis = jedisPool.getResource();

        String set = jedis.set("hello", "world");
        System.out.println("给redis中保存了数据..."+set);

        String hello = jedis.get("hello");
        System.out.println("从redis中获取hello的值是："+hello);
    }
}
