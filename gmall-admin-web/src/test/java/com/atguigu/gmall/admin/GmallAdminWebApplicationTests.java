package com.atguigu.gmall.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallAdminWebApplicationTests {

    @Test
    public void contextLoads() {
//        String asHex = DigestUtils.md5DigestAsHex("123456".getBytes());
//        System.out.println(asHex);

//        String format = String.format("%4d", 1).replace("","0");
//        System.out.println(format);

//        NumberFormat format = DecimalFormat.getNumberInstance();
//        format.set

        NumberFormat numberFormat = DecimalFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        String format = numberFormat.format(1);
        System.out.println(format);
    }

}
