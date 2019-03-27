package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.mapper.CouponHistoryMapper;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.sms.vo.SmsCouponHistoryQueryParam;
import com.atguigu.gmall.vo.GlobalPageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class CouponHistoryServiceImpl extends ServiceImpl<CouponHistoryMapper, CouponHistory> implements CouponHistoryService {

    @Override
    public GlobalPageVo pageCouponHistory(SmsCouponHistoryQueryParam couponHistoryQueryParam, Integer pageNum, Integer pageSize) {
        QueryWrapper<CouponHistory> queryWrapper = new QueryWrapper<>();

        Integer useStatus = couponHistoryQueryParam.getUseStatus();
        if (useStatus != null)
            queryWrapper.eq("use_status", useStatus);

        String orderSn = couponHistoryQueryParam.getOrderSn();
        if (!StringUtils.isEmpty(orderSn))
            queryWrapper.likeRight("order_sn", orderSn);

        Long couponId = couponHistoryQueryParam.getCouponId();
        if (couponId != null)
            queryWrapper.eq("coupon_id", couponId);

        return new GlobalPageVo(baseMapper.selectPage(new Page<CouponHistory>(pageNum, pageSize), queryWrapper));
    }
}
