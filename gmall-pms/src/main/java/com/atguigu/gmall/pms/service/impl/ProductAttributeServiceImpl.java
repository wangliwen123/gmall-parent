package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.mapper.ProductAttributeMapper;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Component
@Service
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

    @Override
    public Map<String, Object> selectProductAttributeByCategory(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        ProductAttributeMapper baseMapper = getBaseMapper();
        IPage<ProductAttribute> page = baseMapper.selectPage(new Page<ProductAttribute>(pageNum, pageSize),
                new QueryWrapper<ProductAttribute>().eq("product_attribute_category_id", cid)
                        .eq("type", type)
        );
        //封装分页数据
        return PageUtils.getPageMap(page);
    }
}
