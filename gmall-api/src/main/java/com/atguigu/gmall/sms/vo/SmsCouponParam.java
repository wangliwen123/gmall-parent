package com.atguigu.gmall.sms.vo;

import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.entity.CouponProductCategoryRelation;
import com.atguigu.gmall.sms.entity.CouponProductRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("创建和修改优惠券时使用的参数")
public class SmsCouponParam extends Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券使用、领取历史设置")
    private List<CouponHistory> couponHistoryList;

    @ApiModelProperty("优惠券和产品分类关系设置")
    private List<CouponProductCategoryRelation> productCategoryRelationList;

    @ApiModelProperty("优惠券和产品的关系设置")
    private List<CouponProductRelation> productRelationList;

}
