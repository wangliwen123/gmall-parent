package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.sms.vo.SmsCouponHistoryQueryParam;
import com.atguigu.gmall.sms.vo.SmsCouponQueryParam;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.vo.GlobalPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author: loy
 * @create: 2019-03-24 20:37:39
 */
@CrossOrigin
@Api(tags = "SmsCouponHistoryController", description = "优惠卷使用、领取历史管理")
@RestController
@RequestMapping("/couponHistory")
public class SmsCouponHistoryController {

    @Reference
    private CouponHistoryService couponHistoryService;

    @ApiOperation("查询优惠卷使用、领取历史")
    @GetMapping(value = "/list")
    public Object getList(
            SmsCouponHistoryQueryParam couponHistoryQueryParam,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        GlobalPageVo couponPageVo = couponHistoryService.pageCouponHistory(couponHistoryQueryParam, pageNum, pageSize);
        return new CommonResult().success(couponPageVo);
    }

}
