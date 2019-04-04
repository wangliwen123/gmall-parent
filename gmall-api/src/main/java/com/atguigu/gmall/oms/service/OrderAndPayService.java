package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.to.OrderStatusEnume;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.vo.OrderResponseVo;
import com.atguigu.gmall.vo.OrderSubmitVo;

import java.util.List;

public interface OrderAndPayService {
    List<MemberReceiveAddress> getUserRecieveAddress(String token);

    String geiwoTradeToken();

    OrderResponseVo createOrder(OrderSubmitVo orderSubmitVo);

    String payMyOrder(String out_trade_no, String total_amount, String subject, String body);

    void updateOrderStatus(String out_trade_no, OrderStatusEnume finished);

    Order getOrderByOrderSn(String orderSn);
}
