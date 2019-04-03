package com.atguigu.gmall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.NumberFormat;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GmallOrderApplicationTests {

    @Test
    public void contextLoads() {
        NumberFormat numberFormat = NumberFormat.getInstance();

        numberFormat.setMaximumIntegerDigits(9);
        numberFormat.setMinimumIntegerDigits(9);

        final String number_suffix = numberFormat.format(1);
        System.out.println(number_suffix.replace(",",""));
    }

}
