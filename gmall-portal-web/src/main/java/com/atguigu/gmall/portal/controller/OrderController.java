package com.atguigu.gmall.portal.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.ums.service.MemberService;
import com.atguigu.gmall.vo.OrderConfirmPageVo;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Api(tags = "订单服务")
@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderAndPayService orderAndPayService;

    @Reference
    CartService cartService;

    @Reference
    MemberService memberService;


    /**
     * 订单确认页需要的所有数据
     * @param token
     * @param result
     * @return
     *
     * 1）、SpringMVC封装的所有参数都会放在ModelAttribute中
     * 2）、默认使用的key
     *
     *
     * @Valid
     * @NotEmpty(message = "此操作必须先登陆")
     */
    @ResponseBody
    @PostMapping("/orderconfirm")
    public CommonResult jiesuan(@RequestParam(value = "token")
                                            String token){
        //去结算确认页，返回结算页的数据

        //1、需要结算的商品信息、目前是获取到的购物车里面的商品的信息
        List<CartItem> cartItems = cartService.cartItemsForJieSuan(token);
        //2、查优惠券


        //3、用户可选的地址列表
       // List<MemberReceiveAddress> memberReceiveAddresses =  memberService.getUserAddress(token);
        List<MemberReceiveAddress> memberReceiveAddresses = orderAndPayService.getUserRecieveAddress(token);

        //3、一个临时令牌。方便下次进行防重验证。
        //dubbo认为getXXX不带参数，是获取他里面的属性
        RpcContext.getContext().setAttachment("gmallusertoken",token);
        String tradeToken = orderAndPayService.geiwoTradeToken();

        return new CommonResult().success(new OrderConfirmPageVo(cartItems,memberReceiveAddresses,tradeToken));
    }

}
