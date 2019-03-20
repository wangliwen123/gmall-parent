package com.atguigu.gmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.admin.ums.vo.UmsAdminLoginParam;
import com.atguigu.gmall.admin.ums.vo.UmsAdminParam;
import com.atguigu.gmall.admin.utils.JwtTokenUtil;
import com.atguigu.gmall.ums.entity.Admin;
import com.atguigu.gmall.ums.service.AdminService;
import com.atguigu.gmall.to.CommonResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台用户管理
 *
 * 1、什么叫跨域?
 *      域：域名； 跨域名访问
 *      跨域出现什么问题？  请求url地址和当前所在的域名不一样 http://www.atguigu.com:8080
 *          www.gulishop.com
 *          <a href="http://www.baidu.com">去百度</a> 可以
 *
 *          <a id="btn01">把百度的东西拿来</a>
 *          $("#btn01").click(function(){
 *              $.get("http://www.baidu.com",function(data){
 *                  // ele.html(data)
 *              })
 *          })
 *
 *      HTTP协议为了安全起见，限制了ajax请求的跨域访问；ajax只能给自己当前网站发请求;
 *      1）、浏览器一但发现是ajax请求，浏览器会帮你发请求，但是返回的数据会被浏览器劫持（服务器默认说不能跨域访问）；
 *      www.ta0ba0.com  www.taobao.com
 *      $.ajax("",function(){
 *
 *      })
 *
 *      2）、场景：难道ajax不能调第三方api？服务器告诉浏览器这次跨域允许
 *              服务器告诉浏览器（响应头） Set-Cookie:k=v
 *
 *
 * 2、为什么会跨域？
 *      请求url地址和当前所在的域名不一样 http://www.atguigu.com:8080
 * 3、怎么解决跨域？
 *      服务器在响应请求的时候，如果某个允许跨域需要在响应头上说明
 */
@CrossOrigin
@RestController
@Api(tags = "AdminController", description = "后台用户管理")
@RequestMapping("/admin")
@Slf4j
public class UmsAdminController {
    @Reference
    private AdminService adminService;



    @Value("${gmall.jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${gmall.jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public Object register(@Valid @RequestBody UmsAdminParam umsAdminParam, BindingResult result) {
        Admin admin = null;
        //TODO 完成注册功能

//        if(!errors){
//
//        }else{
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            Map<String,String> e = new HashMap<>();
//            for (FieldError fieldError : fieldErrors) {
//                //获取属性名 代表当前属性出错了
//                String field = fieldError.getField();
//                //获取当时不满足要求的这个值
//                Object value = fieldError.getRejectedValue();
//                //获取提示信息
//                String message = fieldError.getDefaultMessage();
//                e.put(field,message);
//                log.error("属性校验发生错误：属性名[{}]，属性值[{}]，错误提示消息：{}",field,value,message);
//            }
//
//            return new CommonResult().validateFailed(result);
//        }
            int i = 10 /0;


        //统一的异常处理

        return new CommonResult().success(admin);
    }



    /**
     * 默认不允许跨域
     * @param umsAdminLoginParam
     * @param result
     * @return
     */
    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    public Object login(@RequestBody UmsAdminLoginParam umsAdminLoginParam,
                        BindingResult result) {
        //去数据库登陆
        Admin admin = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());

        //登陆成功生成token，此token携带基本用户信息，以后就不用去数据库了
        String token = jwtTokenUtil.generateToken(admin);

        // token=UUID   作为key 在redis中保存了用户的 username等详细信息；
        // jwt；JSON Web Token； token有意义；   header.payload(负载).sign(签名)
        //把所有的东西制作成jwt。eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiSm9obiBEb2UiLCJkYXRlIjoxNTE2MjM5MDIyfQ.j_q_Trbn0S7olG4NSvGUEmrcXpT4HBjtJZRpuJWPh34
        //让前端以后所有请求都带上这个有意义的token（jwt）；后端不用存储信息
        //jwt安全
        if (token == null) {
            return new CommonResult().validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @GetMapping(value = "/token/refresh")
    public Object refreshToken(HttpServletRequest request) {
        //1、获取请求头中的Authorization完整值
        String oldToken = request.getHeader(tokenHeader);
        String refreshToken = "";

        //2、从请求头中的Authorization中分离出jwt的值
        String token = oldToken.substring(tokenHead.length());

        //3、是否可以进行刷新（未过刷新时间）
        if (jwtTokenUtil.canRefresh(token)) {
            refreshToken =  jwtTokenUtil.refreshToken(token);
        }else  if(refreshToken == null && "".equals(refreshToken)){
            return new CommonResult().failed();
        }

        //将新的token交给前端
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return new CommonResult().success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Object getAdminInfo(HttpServletRequest request) {
        String oldToken = request.getHeader(tokenHeader);

        String token = oldToken.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUserNameFromToken(token);
        System.out.println("需要去访问的用户名："+userName);

        //MyBatisPlus的service简单方法可以用，复杂的方法（参数是QueryWrapper、参数是IPage的都不要用）
       // Admin umsAdmin = adminService.getOne(new QueryWrapper<Admin>().eq("username",userName));
        //Admin umsAdmin = adminService.getById(1);
        Admin umsAdmin = adminService.getAdminByUsername(userName);


        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("roles", new String[]{"TEST"});
        data.put("icon", umsAdmin.getIcon());
        return new CommonResult().success(data);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        //TODO 用户退出

        return new CommonResult().success(null);
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "name",required = false) String name,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        //TODO 分页查询用户信息

        //TODO 响应需要包含分页信息；详细查看swagger规定
        return new CommonResult().failed();
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Object getItem(@PathVariable Long id){

        //TODO 获取指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("更新指定用户信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable Long id,@RequestBody Admin admin){

        //TODO 更新指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@PathVariable Long id){
        //TODO 删除指定用户信息
        return new CommonResult().failed();
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update",method = RequestMethod.POST)
    @ResponseBody
    public Object updateRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds){
        //TODO 给用户分配角色
        return new CommonResult().failed();
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/role/{adminId}",method = RequestMethod.GET)
    @ResponseBody
    public Object getRoleList(@PathVariable Long adminId){
        //TODO 获取指定用户的角色

        return new CommonResult().success(null);
    }

    @ApiOperation("给用户分配(增减)权限")
    @RequestMapping(value = "/permission/update",method = RequestMethod.POST)
    @ResponseBody
    public Object updatePermission(@RequestParam Long adminId,
                                   @RequestParam("permissionIds") List<Long> permissionIds){
        //TODO 给用户分配(增减)权限

        return new CommonResult().failed();
    }

    @ApiOperation("获取用户所有权限（包括+-权限）")
    @RequestMapping(value = "/permission/{adminId}",method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long adminId){
        //TODO 获取用户所有权限（包括+-权限）
        return new CommonResult().failed();
    }
}
