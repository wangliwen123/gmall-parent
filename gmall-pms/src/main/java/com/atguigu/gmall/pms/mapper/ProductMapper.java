package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductMapper extends BaseMapper<Product> {


    List<EsProductAttributeValue> getProductSaleAttr(Long productId);

    List<EsProductAttributeValue> getProductBaseAttr(Long productId);
}
