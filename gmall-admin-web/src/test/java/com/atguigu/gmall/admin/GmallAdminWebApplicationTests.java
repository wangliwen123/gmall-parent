package com.atguigu.gmall.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallAdminWebApplicationTests {

    @Test
    public void contextLoads() {
        String asHex = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(asHex);
    }

}
