package com.atguigu.gmall.search;

import com.atguigu.gmall.to.es.EsProduct;

import java.util.List;

public interface GmallSearchService {


    void publishStatus(List<Long> ids, Integer publishStatus);

    boolean saveProductInfoToES(EsProduct esProduct);
}
