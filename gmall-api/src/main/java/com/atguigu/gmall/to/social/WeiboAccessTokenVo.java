package com.atguigu.gmall.to.social;

import lombok.Data;

@Data
public class WeiboAccessTokenVo extends AccessTokenVo {

//    {
//        "access_token": "2.00pDpxyG0DfSax967043b949wSzOOE",
//            "remind_in": "157679999",
//            "expires_in": 157679999,
//            "uid": "6397634785",
//            "isRealName": "true"
//    }

    private String remind_in;
    private String expires_in;
    private String uid;
    private String isRealName;
}
