package com.atguigu.gmall.item.to;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductAllInfos implements Serializable {

    //当前商品的详情
    private Product product;

    //当前Sku的详情
    private SkuStock skuStock;


    //当前商品所有Sku的组合
    private List<SkuStock> skuStocks;

    //销售属性
    private List<EsProductAttributeValue> saleAttr;

    //这个商品对应的所有属性的值
    private List<EsProductAttributeValue> baseAttr;

    //1、
}
