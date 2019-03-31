package com.atguigu.gmall.controller;


import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HelloController {

    /**
     *   meinv?token
     * @param session
     * @return
     */
    @GetMapping("/meinv")
    public String meinv(HttpSession session,
                        @RequestParam(value = "token",required = false) String token,
                        HttpServletResponse response){

        if(StringUtils.isEmpty(token)){

            return "redirect:http://www.gmallshop.com/login.html?url=http://www.java-sys.com/meinv";
        }else {
            response.addCookie(new Cookie("gmallsso",token));

            return "protected";
        }

    }

    @GetMapping("/chaoji")
    public String chaojimeinv(@CookieValue("gmallsso")String token){
        if(!StringUtils.isEmpty(token)){
            return "chaoji";
        }else {
            return "redirect:http://www.gmallshop.com/login.html?url=http://www.java-sys.com/chaoji";
        }

    }
}
