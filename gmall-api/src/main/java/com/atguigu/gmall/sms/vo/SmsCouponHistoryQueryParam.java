package com.atguigu.gmall.sms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("优惠卷使用、领取历史列表页条件查询参数")
public class SmsCouponHistoryQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("使用状态：0->未使用；1->已使用；2->已过期")
    private Integer useStatus;

    @ApiModelProperty("订单号码")
    private String orderSn;

    @ApiModelProperty("优惠券id")
    private Long couponId;

}
