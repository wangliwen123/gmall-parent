package com.atguigu.gmall.to.es;

import lombok.Data;

import java.io.Serializable;

/**
 * 检索前端传递的数据
 */
@Data
public class SearchParam implements Serializable {


    private Long catelog3Id;//三级分类id

    private Long brandId;//品牌id

    private String keyword;//检索的关键字

    // order=1:asc  排序规则
    private String order;// 0：综合排序  1：销量  2：价格  3：价格区间

    private Integer pageNum = 1;//分页信息

    //props=2:全高清&
    private String[] props;//页面提交的数组

    private Integer pageSize = 12;






}
