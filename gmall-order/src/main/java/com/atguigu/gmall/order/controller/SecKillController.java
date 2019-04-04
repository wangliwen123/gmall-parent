package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.to.CommonResult;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sec")
public class SecKillController {


    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/kill")
    public CommonResult seckill(String token,Long skuId){

        RSemaphore semaphore = redissonClient.getSemaphore(RedisCacheConstant.SEC_KILL + skuId + "");
        //10:00-12:00  skuId  10->hash->skuid1,skuid2
        //get(10)

        boolean b = semaphore.tryAcquire();
        if(b){
            //给mq发送消息
            //setnx(token,skuId)；防止用户秒多个；用户10s发一次请求；
            //setnxex(token,30);控制频率
            //gulishop:user:info:7480e4bd-3d73-41a6-88d9-a5035140f9d8
            System.out.println("用户："+token+";秒杀完成了商品："+skuId);

            //发送消息 新建一个秒杀交换机和对列   token,Long skuId
            //订单服务消费秒杀队列（token,Long skuId）为这个用户创建订单。
        }
        return b?new CommonResult().success("秒杀完成"):new CommonResult().failed();
    }
}
