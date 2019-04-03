package com.atguigu.gmall.vo;

import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmPageVo implements Serializable {

    List<CartItem> cartItem;
    List<MemberReceiveAddress> memberReceiveAddresses;
    //用户可选优惠券
    String tradeToken;//交易令牌
}
