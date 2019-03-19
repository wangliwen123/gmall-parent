package com.atguigu.gmall.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.ums.entity.Role;
import com.atguigu.gmall.ums.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Reference
    RoleService roleService;

    @GetMapping("/list")
    public List<Role> hello(){
        List<Role> list = roleService.list();
        return list;
    }
}
