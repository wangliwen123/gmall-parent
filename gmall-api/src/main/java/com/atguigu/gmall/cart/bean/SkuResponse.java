package com.atguigu.gmall.cart.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuResponse  implements Serializable  {

    private CartItem item;
    private String cartKey;//购物车的key
}
