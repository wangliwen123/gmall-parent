package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.portal.config.AlipayConfig;
import com.atguigu.gmall.to.OrderStatusEnume;
import com.atguigu.gmall.vo.OrderResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    OrderAndPayService orderAndPayService;

    @ResponseBody
    @RequestMapping("/order")
    public String payOrder(OrderResponseVo orderResponseVo){

        String payHtml = orderAndPayService.payMyOrder(orderResponseVo.getOut_trade_no(),
                orderResponseVo.getTotal_amount(), orderResponseVo.getSubject(),
                orderResponseVo.getBody());
        return payHtml;
    }


    /**
     * 异步通知支付宝会调用很多次
     *
     *  3s,15s,1min,5min,30；只要有一次做完事情，告诉支付宝
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping("/async/success")
    public String paySuccess(HttpServletRequest request) throws UnsupportedEncodingException {

        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //以上将支付宝发来的请求的所有数据封装起来



        boolean signVerified = true;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
                    AlipayConfig.sign_type);
            System.out.println("验签：" + signVerified);

        } catch (AlipayApiException e) {
            System.out.println("验签失败....");
        }
        // 商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 支付宝流水号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");


        //改订单的状态
        if (trade_status.equals("TRADE_FINISHED")) {
            //交易完成  刚付完款TRADE_SUCCESS，7天以后，钱到商户的账号上TRADE_FINISHED；
            //锁库存===管理员商品出库（扣库存）===订单全完成，完全扣库存
            orderAndPayService.updateOrderStatus(out_trade_no,OrderStatusEnume.FINISHED);
            //流水日志记录表;
            System.out.println("订单号："+out_trade_no+"交易成功....");

        } else if (trade_status.equals("TRADE_SUCCESS")) {
            //交易成功
            orderAndPayService.updateOrderStatus(out_trade_no,OrderStatusEnume.PAYED);
            System.out.println("订单号："+out_trade_no+"交易成功....");
            //

        }
        // 只要支付宝收到success就不会再通知；
        return "success";
    }



}
