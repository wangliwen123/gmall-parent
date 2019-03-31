package com.atguigu.gmall.cas.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "oauth.weibo")
@Configuration
@Data
public class WeiboOAuthConfig {

   private String appKey;
   private String appSecret;
   private String authSuccessUrl;
   private String authSuccessFail;
   private String authPage;
   private String accessTokenPage;



}
