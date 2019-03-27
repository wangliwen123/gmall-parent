package com.atguigu.gmall.to.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 检索前端传递的数据
 */
@Data
public class SearchParam implements Serializable {

    /**
     * [
     *         {"term": {
     *           "productCategoryId": "19"
     *         }},
     *         {"term": {
     *           "brandId": "51"
     *         }},
     *         {"nested":{
     *           "path":"attrValueList",
     *
     *         }}
     *         ]
     */

    // search?catelog3Id=1&catelog3Id=2&catelog3Id=3&brandId=1&brandId=2&props=2:3g-4g-5g&props=4:4.7-5.0
    private Long[] catelog3Id;//三级分类id

    private Long[] brandId;//品牌id

    private String keyword;//检索的关键字

    // order=1:asc  排序规则
    private String order;// 0：综合排序  1：销量  2：价格  3：价格区间  如果有3:asc

    private Integer pageNum = 1;//分页信息

    //props=2:全高清&  如果前端想传入很多值    props=2:青年-老人-女士
    private String[] props;//页面提交的数组

    private Integer pageSize = 12;

    private Integer priceFrom;//价格区间开始
    private Integer priceTo;//价格区间结束






}
