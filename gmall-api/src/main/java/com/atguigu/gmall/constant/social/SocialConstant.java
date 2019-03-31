package com.atguigu.gmall.constant.social;


/**
 * 社交使用的常量信息
 */
public class SocialConstant {

    public  enum  SocialTypeEnum{
        QQ("1","qq"),WEIBO("2","weibo");
        private String id;
        private String type;
        SocialTypeEnum(String s, String qq) {
            this.id = s;
            this.type = qq;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }


}
