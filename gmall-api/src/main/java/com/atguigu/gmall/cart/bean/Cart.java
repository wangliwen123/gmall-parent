package com.atguigu.gmall.cart.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 购物车
 */
@Setter
public class Cart  implements Serializable {

    @Getter
    private List<CartItem> items;//购物项


    private Integer total;//商品总数
    private BigDecimal totalPrice;//商品总价格

    public Integer getTotal(){
        AtomicReference<Integer> count = new AtomicReference<Integer>(0);
        if(items!=null&&items.size()>0){
            items.forEach((i)->{
                count.set(i.getNum()+count.get());
            });
        }

        return count.get();
    }

    public BigDecimal getTotalPrice() {

        AtomicReference<BigDecimal> price = new AtomicReference<BigDecimal>(new BigDecimal("0"));
        if(items!=null&&items.size()>0){
            items.forEach((i)->{
                BigDecimal price1 = i.getPrice();
                BigDecimal multiply = price1.multiply(new BigDecimal("" + i.getNum()));

                price.set(multiply.add(price.get()));
            });
        }
        return price.get();
    }
}
