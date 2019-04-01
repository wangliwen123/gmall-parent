package com.atguigu.gmall.cart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.classmate.GenericType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallCartApplicationTests {

    //@Autowired
    RedissonClient client;

    @Test
    public void testJson(){
        Set<String> strings = new HashSet<>();
        strings.add("134");strings.add("456");

        String s = JSON.toJSONString(strings);
        System.out.println(s);

        Set<String> strings1 = JSON.parseObject(s, new TypeReference<Set<String>>() {
        });

        System.out.println(strings1);
    }

    @Test
    public void contextLoads() {
//        client.getList()
        RMap<String, String> cart = client.getMap("hello");//new HashMap();
       // cart.put(1,2);

       // cart.remove(1);
        cart.put("124","dadaaaaa");





//        Map<String,Object> map = new HashMap<>();
//        map.put("1","2");


    }

}
