package com.atguigu.gmall.search;

import com.atguigu.gmall.to.es.EsProduct;
import com.atguigu.gmall.to.es.SearchParam;
import com.atguigu.gmall.to.es.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface GmallSearchService {


    void publishStatus(List<Long> ids, Integer publishStatus);

    boolean saveProductInfoToES(EsProduct esProduct);

    /**
     * 检索商品
     * @param param
     * @return
     */
    SearchResponse searchProduct(SearchParam param) throws IOException;
}
