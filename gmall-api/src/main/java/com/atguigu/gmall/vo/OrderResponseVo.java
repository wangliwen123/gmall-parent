package com.atguigu.gmall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponseVo implements Serializable {

    private String tips;
    private Integer code;


    /**
     * String out_trade_no, String total_amount, String subject, String body
     */
    private String out_trade_no;//订单号交给前端
    private String total_amount;//总金额
    private String subject;//主题
    private String body;//


}
