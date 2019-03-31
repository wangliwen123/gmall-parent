package com.atguigu.gmall.ums.mapper;

import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberSocial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-30
 */
public interface MemberSocialMapper extends BaseMapper<MemberSocial> {

    Member getMemberInfo(String uid);

    /**
     * 事务模式下的for update 阻塞查询
     * @param access_token
     * @return
     */
    List<MemberSocial> selectAccessTokenForUpdate(String access_token);
}
