package com.atguigu.gmall.admin.pms.vo;

import com.atguigu.gmall.to.PmsProductParam;
import lombok.Data;

/**
 * 查询单个产品进行修改时返回的结果
 */
@Data
public class PmsProductResult extends PmsProductParam {
    //商品所选分类的父id
    private Long cateParentId;

}
