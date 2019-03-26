package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.constant.EsConstant;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.search.GmallSearchService;
import com.atguigu.gmall.to.es.EsProduct;
import com.atguigu.gmall.to.es.SearchParam;
import com.atguigu.gmall.to.es.SearchResponse;
import com.atguigu.gmall.to.es.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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

    @Override
    public SearchResponse searchProduct(SearchParam param) throws IOException {
        
        //1、根据页面传递的参数构建检索的DSL语句
        String queryDSL = buildSearchDsl(param);
        Search search = new Search.Builder(queryDSL).build();

        //2、执行查询
        SearchResult result = jestClient.execute(search);

        //3、封装和分析查询结果
        SearchResponse response = buildSearchResult(result);

        //4、封装分页信息
        response.setPageNum(param.getPageNum());
        response.setPageSize(param.getPageSize());
        response.setTotal(result.getTotal());


        return response;
    }

    /**
     * 封装检索的结果
     * @param result
     * @return
     */
    private SearchResponse buildSearchResult(SearchResult result) {
        System.out.println(result.getTotal()+"==>"+result.toString());
        SearchResponse searchResponse = new SearchResponse();
        //1、封装所有的商品信息
        List<SearchResult.Hit<EsProduct, Void>> hits = result.getHits(EsProduct.class);
        for (SearchResult.Hit<EsProduct, Void> hit : hits) {
            EsProduct source = hit.source;
            searchResponse.getProducts().add(source);
        }

        /**
         *  {name:"品牌",values:["小米","苹果"]}
         */
        //2、封装属性信息
        //2.1）、封装品牌进response
        MetricAggregation aggregations = result.getAggregations();

        SearchResponseAttrVo brandId = new SearchResponseAttrVo();
        brandId.setName("品牌");
        //2.2）、获取到品牌
        aggregations.getTermsAggregation("brandIdAgg").getBuckets().forEach((b)->{
            b.getTermsAggregation("brandNameAgg").getBuckets().forEach((bb)->{
                String key = bb.getKey();
                brandId.getValue().add(key);
            });
        });
        searchResponse.setBrand(brandId);


        //2.3）、获取到分类
        SearchResponseAttrVo category = new SearchResponseAttrVo();
        category.setName("分类");
        aggregations.getTermsAggregation("categoryIdAgg").getBuckets().forEach((b)->{
            b.getTermsAggregation("productCategoryNameAgg").getBuckets().forEach((bb)->{
                String key = bb.getKey();
                category.getValue().add(key);
            });
        });
        searchResponse.setCatelog(category);


        //2.4）、获取到属性
        TermsAggregation termsAggregation = aggregations.getChildrenAggregation("productAttrAgg")
                .getChildrenAggregation("productAttrIdAgg")
                .getTermsAggregation("productAttrIdAgg");

//        Bucket bucket = childrenAggregation.getAggregation("productAttrId", Bucket.class);
        List<TermsAggregation.Entry> buckets = termsAggregation.getBuckets();
        buckets.forEach((b)->{
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            //第一层属性id
            attrVo.setProductAttributeId(Long.parseLong(b.getKey()));
            b.getTermsAggregation("productAttrNameAgg").getBuckets().forEach((bb)->{
                //第二层是属性的名
                attrVo.setName(bb.getKey());
                bb.getTermsAggregation("productAttrValueAgg").getBuckets().forEach((bbb)->{
                    //第三层是属性的值
                    attrVo.getValue().add(bbb.getKey());
                });
            });

            searchResponse.getAttrs().add(attrVo);
        });


        return searchResponse;
    }

    /**
     * 构建检索条件
     * @param param
     * @return
     */
    private String buildSearchDsl(SearchParam param) {

        SearchSourceBuilder searchSource = new SearchSourceBuilder();

        //1、查询
        searchSource.query(QueryBuilders.matchQuery("name",param.getKeyword()));

        //2、过滤

        //2、聚合
        //searchSource.aggregation()
        //2.1、聚合品牌信息
        TermsAggregationBuilder brandAggs = AggregationBuilders.terms("brandIdAgg")
                .field("brandId")
                .size(100)
                .subAggregation(
                        AggregationBuilders.terms("brandNameAgg")
                                .field("brandName")
                                .size(10)
                );
        searchSource.aggregation(brandAggs);

        //2.2）、聚合分类信息
        TermsAggregationBuilder categoryAggs = AggregationBuilders.terms("categoryIdAgg")
                .field("productCategoryId")
                .size(100)
                .subAggregation(
                        AggregationBuilders.terms("productCategoryNameAgg")
                                .field("productCategoryName")
                                .size(100)
                );
        searchSource.aggregation(categoryAggs);


        //2.3）、属性聚合
        FilterAggregationBuilder filter = AggregationBuilders
                .filter(
                        "productAttrIdAgg",
                        QueryBuilders.termQuery("attrValueList.type", "1"
                        )
                );

        filter.subAggregation(AggregationBuilders.terms("productAttrIdAgg")
                    .field("attrValueList.productAttributeId")
                    .size(100)
                        .subAggregation(
                                AggregationBuilders.terms("productAttrNameAgg")
                                    .field("attrValueList.name").size(100)
                                        .subAggregation(
                                                AggregationBuilders.terms("productAttrValueAgg")
                                                .field("attrValueList.value").size(100)
                                        )
                        )
        );

        //2.3）、属性聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("productAttrAgg", "attrValueList")
                .subAggregation(filter);
        searchSource.aggregation(attrAgg);

        //3、高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name").preTags("<b style='color:red'>").postTags("</b>");
        searchSource.highlighter(highlightBuilder);

        //4、分页  0,5  5,5  10,5
        //param.getPageNum()
        searchSource.from((param.getPageNum()-1)*param.getPageSize());
        searchSource.size(param.getPageSize());

        System.out.println(searchSource.toString());
        return searchSource.toString();
    }
}
