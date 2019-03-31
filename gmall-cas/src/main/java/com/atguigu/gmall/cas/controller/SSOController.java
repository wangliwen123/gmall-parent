package com.atguigu.gmall.cas.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class SSOController {

    Map<String,Object> users = new HashMap<>();

    @GetMapping("/login.html")
    public String loginPage(String url,@CookieValue(value = "gmallsso",required = false) String gmallsso){
        //登陆成功去url
        Object o = users.get(gmallsso);
        if(o != null){
            //有cookie说明登陆了
            return  "redirect:"+url+"?token="+gmallsso;
        }
        return  "login";
    }

    @PostMapping("/logintosys")
    public String login(String username, String password, String url, HttpSession session, HttpServletResponse response){
        Map<String,Object> user  = new HashMap<>();
        user.put("username",username);
        user.put("password",password);

        session.setAttribute("loginUser",user);
        System.out.println("收到的数据..."+url);
        String s = UUID.randomUUID().toString();
        users.put(s,user);
        Cookie cookie = new Cookie("gmallsso",s);

        response.addCookie(cookie);
        return "redirect:"+url;
    }
}
