package com.atguigu.gmall.cart;

import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.SkuResponse;

public interface CartService {
    SkuResponse addToCart(Long skuId, Integer num, String token);

    boolean updateCount(Long skuId, Integer num, String cartKey);

    boolean deleteCart(Long skuId, String cartKey);

    boolean checkCart(Long skuId, Integer flag, String cartKey);

    Cart cartItemsList(String cartKey);
}
