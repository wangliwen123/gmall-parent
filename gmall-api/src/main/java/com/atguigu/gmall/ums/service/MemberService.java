package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.ums.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface MemberService extends IService<Member> {

    Member login(String username, String password);
}
