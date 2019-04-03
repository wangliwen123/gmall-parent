package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.ums.entity.MemberReceiveAddress;

import java.util.List;

public interface OrderAndPayService {
    List<MemberReceiveAddress> getUserRecieveAddress(String token);

    String geiwoTradeToken();
}
