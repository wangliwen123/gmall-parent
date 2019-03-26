package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.to.PmsProductParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductService extends IService<Product> {

    //分页查询出来的数据
    Map<String, Object> pageProduct(Integer pageSize, Integer pageNum);

    /**
     * 给数据库保存商品信息
     * @param productParam
     */
    void create(PmsProductParam productParam);

    void publishStatus(List<Long> ids, Integer publishStatus);

//    public Long saveProduct(PmsProductParam productParam);
//    public void saveOther(PmsProductParam productParam);
}
