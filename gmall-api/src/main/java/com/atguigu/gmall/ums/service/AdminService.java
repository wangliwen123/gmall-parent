package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.ums.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface AdminService extends IService<Admin> {




    //登陆
    Admin login(String username, String password);

    //按照用户名查用户
    Admin getAdminByUsername(String userName);
}
