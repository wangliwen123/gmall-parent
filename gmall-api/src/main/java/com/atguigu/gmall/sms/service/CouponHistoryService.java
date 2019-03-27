package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.vo.SmsCouponHistoryQueryParam;
import com.atguigu.gmall.vo.GlobalPageVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author: loy
 * @create: 2019-03-24 20:43:12
 */
public interface CouponHistoryService extends IService<CouponHistory> {

    /**
     * 查询优惠卷使用、领取历史
     *
     * @param couponHistoryQueryParam
     * @param pageNum
     * @param pageSize
     * @return
     */
    GlobalPageVo pageCouponHistory(SmsCouponHistoryQueryParam couponHistoryQueryParam, Integer pageNum, Integer pageSize);
}
