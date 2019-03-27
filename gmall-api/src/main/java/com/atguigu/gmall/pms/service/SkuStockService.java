package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.SkuStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface SkuStockService extends IService<SkuStock> {

    List<SkuStock> getAllSkuInfoByProductId(Long productId);
}
