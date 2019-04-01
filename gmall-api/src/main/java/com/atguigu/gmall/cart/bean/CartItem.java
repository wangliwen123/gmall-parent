package com.atguigu.gmall.cart.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物项
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long productId;
    private Long productSkuId; //skuId
    private Long memberId;
    private Integer num;
    private BigDecimal price;//加入购物车时的价格
    private BigDecimal newPrice;//新价格
    private Integer total;//数量
    private String sp1;//销售属性1
    private String sp2;
    private String sp3;
    private String productPic;//
    private String productName;
    private String memberNickname;
    private Long productCategoryId;
    private String productBrand;
    private Boolean checked; //是否被选中
    private String coupon; //优惠券提示



}
