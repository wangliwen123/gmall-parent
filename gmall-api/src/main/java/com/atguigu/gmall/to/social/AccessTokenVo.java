package com.atguigu.gmall.to.social;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessTokenVo implements Serializable {
   private String access_token;
}
