package com.atguigu.gmall.to.social;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeiboUserVo implements Serializable {
    /**
     * "id": 1404376560,
     *     "screen_name": "zaku",
     *     "name": "zaku",
     *     "province": "11",
     *     "city": "5",
     *     "location": "北京 朝阳区",
     *     "description": "人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。",
     *     "url": "http://blog.sina.com.cn/zaku",
     *     "profile_image_url": "http://tp1.sinaimg.cn/1404376560/50/0/1",
     *     "domain": "zaku",
     *     "gender": "m",
     *     "followers_count": 1204,
     *     "friends_count": 447,
     *     "statuses_count": 2908,
     *     "favourites_count": 0,
     *     "created_at": "Fri Aug 28 00:00:00 +0800 2009",
     *     "following": false,
     *     "allow_all_act_msg": false,
     *     "geo_enabled": true,
     */
    private Long id;
    private String name;
    private String province;
    private String location;
    private String profile_image_url;

}
