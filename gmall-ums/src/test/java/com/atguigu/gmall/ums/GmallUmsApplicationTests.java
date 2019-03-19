package com.atguigu.gmall.ums;

import com.atguigu.gmall.ums.entity.Role;
import com.atguigu.gmall.ums.mapper.RoleMapper;
import io.shardingjdbc.core.api.HintManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallUmsApplicationTests {

    @Autowired
    RoleMapper roleMapper;


    @Test
    public void testwrite(){
        Role role = new Role();
        role.setName("测试角色");
        int insert = roleMapper.insert(role);
        System.out.println("数据插入完成.....");
    }


    @Test
    public void testRead(){
        //强制去主库
       // HintManager.getInstance().setMasterRouteOnly();
        Role role = roleMapper.selectById(6);
        System.out.println(role.getName());
    }


}
