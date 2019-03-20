package com.atguigu.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.ums.entity.Admin;
import com.atguigu.gmall.ums.mapper.AdminMapper;
import com.atguigu.gmall.ums.service.AdminPermissionRelationService;
import com.atguigu.gmall.ums.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {



    @Override
    public Admin login(String username, String password) {
        //Spring自带的md5工具类
        String digest = DigestUtils.md5DigestAsHex(password.getBytes());

        AdminMapper baseMapper = getBaseMapper();

        Admin admin = baseMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", username)
                .eq("password", digest));
        return admin;
    }


    @Override
    public Admin getAdminByUsername(String userName) {

        AdminMapper adminMapper = getBaseMapper();
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", userName));

        return admin;
    }


}
