package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.sms.vo.SmsCouponParam;
import com.atguigu.gmall.sms.vo.SmsCouponQueryParam;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.vo.GlobalPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 优惠券管理 Controller
 *
 * @author: loy
 * @create: 2019-03-24 19:13:58
 */
@CrossOrigin
@Api(tags = "SmsCouponController", description = "优惠卷管理")
@RestController
@RequestMapping("/coupon")
public class SmsCouponController {

    @Reference
    private CouponService couponService;

    @ApiOperation("查询优惠券")
    @GetMapping("/list")
    public Object getList(SmsCouponQueryParam couponQueryParam,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        GlobalPageVo couponPageVo = couponService.pageCoupon(couponQueryParam, pageNum, pageSize);
        return new CommonResult().success(couponPageVo);
    }

    @ApiOperation("创建商品")
    @PostMapping(value = "/create")
    public Object create(@Valid @RequestBody SmsCouponParam couponParam,
                         BindingResult bindingResult) {

        couponService.createOrUpdateCoupon(couponParam);
        return new CommonResult().success("创建商品成功");
    }

    @ApiOperation("修改商品")
    @PostMapping(value = "/update/{couponId}")
    public Object update(@PathVariable Long couponId, @Valid @RequestBody SmsCouponParam couponParam,
                         BindingResult bindingResult) {

        couponService.createOrUpdateCoupon(couponParam);
        return new CommonResult().success("修改商品成功");
    }

    @ApiOperation("根据优惠券id获取优惠券编辑信息")
    @GetMapping("{couponId}")
    public Object getUpdateInfo(@PathVariable Long couponId) {

        SmsCouponParam couponParam = couponService.getUpdateInfo(couponId);
        return new CommonResult().success(couponParam);
    }

    @ApiOperation("删除指定优惠券")
    @DeleteMapping("delete/{couponId}")
    public Object deleteById(@PathVariable Long couponId) {

        return new CommonResult().success(couponService.removeById(couponId));
    }

}
