package com.atguigu.gmall.cart;

import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.cart.bean.SkuResponse;

import java.util.List;

public interface CartService {
    SkuResponse addToCart(Long skuId, Integer num, String token);

    boolean updateCount(Long skuId, Integer num, String cartKey);

    boolean deleteCart(Long skuId, String cartKey);

    boolean checkCart(Long skuId, Integer flag, String cartKey);

    Cart cartItemsList(String cartKey);

    Cart cartItemsForLoginUser(String token);

    List<CartItem> cartItemsForJieSuan(String token);

}
