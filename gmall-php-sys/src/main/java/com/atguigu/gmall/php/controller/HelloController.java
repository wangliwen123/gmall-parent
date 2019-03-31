package com.atguigu.gmall.php.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HelloController {


    @GetMapping("/shuai")
    public String shuaige(HttpSession session){

        Object loginUser = session.getAttribute("loginUser");
        if(loginUser!=null){
            return "protected";
        }else{
            return "redirect:http://www.gmallshop.com/login.html";
        }

    }
}
