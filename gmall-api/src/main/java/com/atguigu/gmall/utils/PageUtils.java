package com.atguigu.gmall.utils;

import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

public class PageUtils {


    public static Map<String, Object> getPageMap(IPage page) {

        Map<String, Object> map = new HashMap<>();
        map.put("pageSize",page);
        map.put("totalPage",page.getPages());
        map.put("total",page.getTotal());
        map.put("pageNum",page.getCurrent());
        map.put("list",page.getRecords());
        return map;
    }
}
