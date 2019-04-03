package com.atguigu.gmall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderSubmitVo implements Serializable {

    private String token;//用户令牌
    private String tradeToken;//交易令牌
    private BigDecimal price;//支付总价
    private Long  addressId;//用户选择的地址
    private String beizhu;//订单的备注信息

    /**
     * ---------------------
     */


}
