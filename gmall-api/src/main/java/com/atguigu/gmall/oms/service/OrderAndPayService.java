package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.vo.OrderResponseVo;
import com.atguigu.gmall.vo.OrderSubmitVo;

import java.util.List;

public interface OrderAndPayService {
    List<MemberReceiveAddress> getUserRecieveAddress(String token);

    String geiwoTradeToken();

    OrderResponseVo createOrder(OrderSubmitVo orderSubmitVo);
}
