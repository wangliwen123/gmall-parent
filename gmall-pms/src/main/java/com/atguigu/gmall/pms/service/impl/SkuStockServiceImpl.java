package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.mapper.SkuStockMapper;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {
    @Autowired
    SkuStockMapper skuStockMapper;

    @Override
    public List<SkuStock> getAllSkuInfoByProductId(Long productId) {

        return  skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id",productId));
    }

    @Override
    public BigDecimal getSkuPriceById(Long skuId) {
        //TODO 查缓存+读写锁

        SkuStock skuStock = skuStockMapper.selectById(skuId);
        return skuStock.getPrice();
    }
}
