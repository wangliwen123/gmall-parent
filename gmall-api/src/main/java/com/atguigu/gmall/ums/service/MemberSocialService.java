package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.to.social.AccessTokenVo;
import com.atguigu.gmall.to.social.WeiboAccessTokenVo;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberSocial;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-30
 */
public interface MemberSocialService extends IService<MemberSocial> {

    Member getMemberInfo(AccessTokenVo tokenVo);
}
