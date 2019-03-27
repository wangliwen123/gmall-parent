package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.search.GmallSearchService;
import com.atguigu.gmall.to.es.SearchParam;
import com.atguigu.gmall.to.es.SearchResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
public class SearchController {

    @Reference(version = "1.0")
    GmallSearchService searchService;

    @GetMapping("/search")
    public SearchResponse search(SearchParam param) throws IOException {


        SearchResponse searchResponse =  searchService.searchProduct(param);



        return searchResponse;
    }
}
