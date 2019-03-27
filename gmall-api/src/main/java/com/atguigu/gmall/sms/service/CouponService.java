package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.vo.SmsCouponParam;
import com.atguigu.gmall.sms.vo.SmsCouponQueryParam;
import com.atguigu.gmall.vo.GlobalPageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠卷表 服务类
 * </p>
 *
 * @author: loy
 * @create: 2019-03-24 19:49:29
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 查询优惠券
     *
     * @param couponQueryParam
     * @param pageNum
     * @param pageSize
     * @return
     */
    GlobalPageVo pageCoupon(SmsCouponQueryParam couponQueryParam, Integer pageNum, Integer pageSize);

    /**
     * 创建或修改商品
     *
     * @param couponParam
     */
    void createOrUpdateCoupon(SmsCouponParam couponParam);

    /**
     * 根据优惠券 id 获取优惠券编辑信息
     *
     * @param couponId
     * @return
     */
    SmsCouponParam getUpdateInfo(Long couponId);

}
