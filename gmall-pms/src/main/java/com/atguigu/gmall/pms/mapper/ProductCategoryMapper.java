package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.ProductCategory;
import com.atguigu.gmall.to.PmsProductCategoryWithChildrenItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    /**
     * 查询并返回数据
     * @return
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren(Integer id);
}
