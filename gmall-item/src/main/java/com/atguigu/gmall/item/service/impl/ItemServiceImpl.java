package com.atguigu.gmall.item.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.item.to.ProductAllInfos;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.to.es.EsProductAttributeValue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(version = "1.0")
public class ItemServiceImpl implements ItemService {

    @Reference
    SkuStockService skuStockService;

    @Reference
    ProductService productService;



    @Override
    public ProductAllInfos getInfo(Long skuId) {
        /**
         * 1、热点数据【经常查】
         * 2、非热点数据【查询频率不高】
         * 缓存；
         *  1）、经常读数据（一定进缓存）
         *  2）、经常改的数据【还是用缓存】【缓存数据库数据的一致性】
         *
         *
         */



        ProductAllInfos infos = new ProductAllInfos();
        //1、当前sku的详细信息查出来；包括销售属性的组合。库存，价格
        SkuStock skuStock = skuStockService.getById(skuId);


        //2、当前商品的详细信息
        Long productId = skuStock.getProductId();

        //引入缓存机制
        //1）、查询商品，直接去缓存中查询
        //2）、如果缓存中没有，去数据库查询。查来的数据再放入缓存，下一个人就不用查了；
        Product product = productService.getProductByIdFromCache(productId);



        //3、所有sku的组合选法以及库存状态
        List<SkuStock> skuStocks = skuStockService.getAllSkuInfoByProductId(productId);

        //4、查询这个商品所有销售属性可选值
        List<EsProductAttributeValue> saleAttr = productService.getProductSaleAttr(productId);


        //5、商品的其他属性值
        List<EsProductAttributeValue> baseAttr = productService.getProductBaseAttr(productId);


        //6、当前商品涉及到的服务

        infos.setSaleAttr(saleAttr);
        infos.setBaseAttr(baseAttr);
        infos.setProduct(product);
        infos.setSkuStocks(skuStocks);
        infos.setSkuStock(skuStock);


        return infos;
    }
}
