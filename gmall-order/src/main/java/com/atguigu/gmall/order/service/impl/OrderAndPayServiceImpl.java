package com.atguigu.gmall.order.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.ums.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Component
public class OrderAndPayServiceImpl implements OrderAndPayService {


    @Reference
    MemberService memberService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public List<MemberReceiveAddress> getUserRecieveAddress(String token) {

        String s = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(s, Member.class);
        if(member!=null){
            return memberService.getUserAddress(member.getId());
        }
        return null;
    }

    @Override
    public String geiwoTradeToken() {
        String gmallusertoken = (String) RpcContext.getContext().get("gmallusertoken");
        String replace = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisCacheConstant.TRADE_TOKEN+gmallusertoken,replace,RedisCacheConstant.TRADE_TOKEN_TIME, TimeUnit.MINUTES);
        return replace;
    }
}
