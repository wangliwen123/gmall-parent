package com.atguigu.gmall.cas.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.to.social.WeiboAccessTokenVo;
import com.atguigu.gmall.cas.config.WeiboOAuthConfig;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.service.MemberSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@CrossOrigin
@Controller
public class OAuth2Controller {


    @Autowired
    WeiboOAuthConfig config;

    @Reference(version = "1.0")
    MemberSocialService memberSocialService;


    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    StringRedisTemplate redisTemplate;
    /**
     *
     *
     *
     *  ?url=http://127.0.0.1:8020/bootstrap_1115/haha.html
     *
     * @param authType  qq/weibo/xx
     * @param url 一但所有流程都结束以后回到我之前的页面
     * @return
     */
    @GetMapping("/register/authorization")
    public String  registerAuthorization(@RequestParam("authType") String authType,
                                         @RequestParam("url") String url, HttpSession session){

        session.setAttribute("url",url);
        if("weibo".equals(authType)){
            return "redirect:"+config.getAuthPage();
        }
        return "redirect:"+config.getAuthPage();

    }

    /**
     *
     * 1、127.0.0.1===》访问的是www.gmallshop.com/register/authorization
     * 2、又访问的是 www.gmallshop.com/register/authorization；
     *      他要用session。命令这次响应保存一个jsessionid=123；但是jsessionid=123仅在有人访问www.gmallshop.com
     *      在其他域看不到jsessionid信息
     * 3、第二步完成给浏览器一个响应。
     *      响应头：Set-Cookie：jsessionid=123（自己给www.gmallshop.com域下保存一个cookie；jsessionid=123）
     *             Location：127.0.0.1（在继续重定向到127.0.0.1）
     * 4、从别的域就算给www.gmallshop.com发请求（ajax）能不用用到www.gmallshop.com里面的cookie
     *      发请求之前ajax就要构造请求，由于当前是127域，所以127的所有cookie会被带上；
     *      ajax--userinfo：获取不到
     *      然而，如果用a直接跳转过去获取能拿到数据？浏览器要跳转，那就是直接域都变了，能获取到数据
     *
     * 问题：如果想使用cookie&session机制，跨域（都耗时）不好使。就算了。
     *      一级域名相同：我们只需要把cookie的作用域放大
     *      一级域名不相同：我们必须采用一个小技巧；
     *
     *
     * 解决：
     *
     *
     *
     * 用户授权通过，会返回一个code码
     * @return
     */

    @GetMapping("/auth/success")
    public String codeGetToken(@RequestParam("code") String code,HttpSession session){
        //或取到code码
        System.out.println("获取到的code码"+code);
        //1、根据这个code码。我们去weibo换取access_token
        //2、换取access_token
        String authPage =  config.getAccessTokenPage()+"&code="+code;
        WeiboAccessTokenVo tokenVo = restTemplate.postForObject(authPage, null, WeiboAccessTokenVo.class);
        //3、加access_token变为UUID，存储到redis中;
        //4、用户第一次进来？ 接下来将这个用户注册进系统

        Member memberInfo = memberSocialService.getMemberInfo(tokenVo);

        //1）、判断这个社交登陆进来的用户是否之前注册过。如果没有自动注册进来
        //2）、如果以前是登陆过的，将用户在我系统里面的信息返回给用户
        //3）、access_token获取到用户的初始信息，然后注册用户的时候使用这些作为初始信息
        //4）、以后社交登陆登陆进来。利用社交的uid查询本系统的用户，返回这个信息
        //数据库也要保存一下access_token，如果过期，重新引导授权，没有过期，直接使用access_token一键分享社交平台

        String url = (String) session.getAttribute("url");
        //session.setAttribute("loginUser",memberInfo);

        //此次响应命令浏览器保存一个cookie；jsessionid=xxxxxadasdada；仅在访问 www.gmallshop.com有效
        //

        //分布式中全部使用令牌机制
        String token = UUID.randomUUID().toString();
        String memberInfoJson = JSON.toJSONString(memberInfo);
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_INFO_CACHE_KEY+token,memberInfoJson);

//        return tokenVo.getAccess_token();
        return "redirect:"+url+"?token="+token;
    }

    /**
     * 登陆成功的用户以后的任何请求都带上token
     * @param token
     * @return
     */
    @ResponseBody
    @GetMapping("/userinfo")
    public Member getUserInfo(String token){

        String memberInfo = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);

        Member member = JSON.parseObject(memberInfo, Member.class);
        return member;
    }
}
