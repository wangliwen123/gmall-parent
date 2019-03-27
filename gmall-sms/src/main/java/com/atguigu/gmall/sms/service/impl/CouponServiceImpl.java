package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.common.util.LoyUtils;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.entity.CouponProductCategoryRelation;
import com.atguigu.gmall.sms.entity.CouponProductRelation;
import com.atguigu.gmall.sms.mapper.CouponMapper;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.sms.service.CouponProductCategoryRelationService;
import com.atguigu.gmall.sms.service.CouponProductRelationService;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.sms.vo.SmsCouponParam;
import com.atguigu.gmall.sms.vo.SmsCouponQueryParam;
import com.atguigu.gmall.vo.GlobalPageVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public GlobalPageVo pageCoupon(SmsCouponQueryParam couponQueryParam, Integer pageNum, Integer pageSize) {

        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();

        // 优惠券类型
        Integer type = couponQueryParam.getType();
        if (type != null)
            queryWrapper.eq("type", type);

        // 优惠券名称
        String name = couponQueryParam.getName();
        if (!StringUtils.isEmpty(name))
            queryWrapper.likeRight("name", name);

        return new GlobalPageVo(baseMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @Override
    public SmsCouponParam getUpdateInfo(Long couponId) {

        SmsCouponParam couponParam = LoyUtils.copyProperties(baseMapper.selectById(couponId), new SmsCouponParam());

        List<CouponHistory> couponHistories = couponHistoryService.list(new QueryWrapper<CouponHistory>().eq("coupon_id", couponId));
        if (!LoyUtils.isEmpty(couponHistories))
            couponParam.setCouponHistoryList(couponHistories);

        List<CouponProductCategoryRelation> couponProductCategoryRelations = couponProductCategoryRelationService.list(new QueryWrapper<CouponProductCategoryRelation>().eq("coupon_id", couponId));
        if (!LoyUtils.isEmpty(couponProductCategoryRelations))
            couponParam.setProductCategoryRelationList(couponProductCategoryRelations);

        List<CouponProductRelation> couponProductRelations = couponProductRelationService.list(new QueryWrapper<CouponProductRelation>().eq("coupon_id", couponId));
        if (!LoyUtils.isEmpty(couponProductRelations))
            couponParam.setProductRelationList(couponProductRelations);

        return couponParam;
    }

    ThreadLocal<Long> couponThreadLocal = new ThreadLocal<>();

    @Transactional
    public void createOrUpdateCoupon(SmsCouponParam couponParam) {
        CouponServiceImpl currentProxy = (CouponServiceImpl) AopContext.currentProxy();

        currentProxy.saveOrUpdateCoupon(couponParam);

        currentProxy.saveOrUpdateBatchCouponHistory(couponParam.getCouponHistoryList());
        currentProxy.saveOrUpdateBatchCouponProductCategoryRelation(couponParam.getProductCategoryRelationList());
        currentProxy.saveOrUpdateBatchCouponProductRelation(couponParam.getProductRelationList());
    }

    /**
     * 保存或修改“优惠券信息”
     *
     * @param couponParam
     */
    @Transactional
    public void saveOrUpdateCoupon(SmsCouponParam couponParam) {
        Long id = couponParam.getId();
        Coupon coupon = LoyUtils.copyProperties(couponParam, new Coupon());

        if (id == null) {
            baseMapper.insert(coupon);
            couponThreadLocal.set(coupon.getId());
        } else
            baseMapper.updateById(coupon);
    }

    @Autowired
    private CouponHistoryService couponHistoryService;

    /**
     * 批量保存或修改“优惠券使用、领取历史信息”
     *
     * @param couponHistories
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateBatchCouponHistory(List<CouponHistory> couponHistories) {
        if (LoyUtils.isEmpty(couponHistories)) return;

        final Long couponId = couponThreadLocal.get();
        if (couponId != null)
            couponHistories = couponHistories.stream().map(couponHistory -> couponHistory.setCouponId(couponId)).collect(Collectors.toList());

        couponHistoryService.saveOrUpdateBatch(couponHistories);
    }

    @Autowired
    private CouponProductCategoryRelationService couponProductCategoryRelationService;

    /**
     * 批量保存或修改“优惠券和产品分类关系”
     *
     * @param couponProductCategoryRelations
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateBatchCouponProductCategoryRelation(List<CouponProductCategoryRelation> couponProductCategoryRelations) {
        if (LoyUtils.isEmpty(couponProductCategoryRelations)) return;

        final Long couponId = couponThreadLocal.get();
        if (couponId != null)
            couponProductCategoryRelations = couponProductCategoryRelations.stream().map(couponProductCategoryRelation -> couponProductCategoryRelation.setCouponId(couponId)).collect(Collectors.toList());

        couponProductCategoryRelationService.saveOrUpdateBatch(couponProductCategoryRelations);
    }

    @Autowired
    CouponProductRelationService couponProductRelationService;

    /**
     * 批量保存或修改“优惠券和产品的关系”
     *
     * @param couponProductRelations
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateBatchCouponProductRelation(List<CouponProductRelation> couponProductRelations) {
        if (LoyUtils.isEmpty(couponProductRelations)) return;

        final Long couponId = couponThreadLocal.get();
        if (couponId != null)
            couponProductRelations = couponProductRelations.stream().map(couponProductRelation -> couponProductRelation.setCouponId(couponId)).collect(Collectors.toList());

        couponProductRelationService.saveOrUpdateBatch(couponProductRelations);
    }

}
