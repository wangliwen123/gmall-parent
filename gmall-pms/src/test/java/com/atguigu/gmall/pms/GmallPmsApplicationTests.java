package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.mapper.ProductCategoryMapper;
import com.atguigu.gmall.to.PmsProductCategoryWithChildrenItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPmsApplicationTests {

    @Autowired
    ProductCategoryMapper productCategoryMapper;

    @Test
    public void contextLoads() {

        List<PmsProductCategoryWithChildrenItem> items = productCategoryMapper.listWithChildren(0);
        System.out.println(items);
    }

}
