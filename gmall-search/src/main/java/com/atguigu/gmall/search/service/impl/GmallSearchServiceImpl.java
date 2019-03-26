package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.constant.EsConstant;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.search.GmallSearchService;
import com.atguigu.gmall.to.es.EsProduct;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Service(version = "1.0")
@Component
public class GmallSearchServiceImpl implements GmallSearchService {

    @Autowired
    JestClient jestClient;

    @Reference
    ProductService productService;


    @Override
    public void publishStatus(List<Long> ids, Integer publishStatus) {

        //1、修改数据库的上下架状态
        ids.forEach((productId)->{
            //传入的是商品id。上架的是sku
            //1）、根据商品id查询sku信息，改写标题，上架到es

            Long id = productId;
            //1）、查询这个商品的详情和他所有的sku
        });



        //2、将商品数据保存到es【】
        // 【1、商品的全量数据（商品需要检索的数据进入ES即可、商品检索需要的一些关联数据也要进来）】
        // 【2、商品的数据哪些需要检索、过滤、排序...】
        //3、搜索展示的是SPU信息？sku销售属性也要筛选？
        //上架一款商品，是将这个SPU下的所有SKU信息全部放在es中
        //SKU商品的标题：SPU的标题+SKU的销售属性
        //  小米8 全面屏智能游戏手机
        //白色 128G , 黑色 256G  透明G
        //name:小米8 全面屏智能游戏手机 白色 128G
        //小米8 全面屏智能游戏手机 黑色 256G



    }

    @Override
    public boolean saveProductInfoToES(EsProduct esProduct) {
        Index index = new Index.Builder(esProduct)
                .index(EsConstant.ES_PRODUCT_INDEX)
                .type(EsConstant.ES_PRODUCT_TYPE)
                .id(esProduct.getId().toString())
                .build();

        DocumentResult execute = null;
        try {
            execute = jestClient.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return execute.isSucceeded();

    }
}
