package com.atguigu.gmall.sms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("优惠券列表页条件查询参数")
public class SmsCouponQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠卷类型；0->全场赠券；1->会员赠券；2->购物赠券；3->注册赠券")
    private Integer type;

    @ApiModelProperty("优惠券名称")
    private String name;

}
