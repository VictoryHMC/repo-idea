package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.ResponseResult;
import com.lagou.domain.Role;
import com.lagou.domain.User;
import com.lagou.domain.UserVO;
import com.lagou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController // @Controller + ResponseBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //用户分页&多条件查询
    @RequestMapping("/findAllUserByPage")   //前端是post，接收请求体中的参数封装到userVO对象中
    public ResponseResult findAllUserByPage(@RequestBody UserVO userVO){

        PageInfo pageInfo = userService.findAllUserByPage(userVO);

        return new ResponseResult(true,200,"分页多条件查询成功",pageInfo);
    }

    /*  修改用户状态
        ENABLE能登录，DISABLE不能登录
     */
    @RequestMapping("/updateUserStatus")
    public ResponseResult updateUserStatus(int id,String status){
        if("ENABLE".equalsIgnoreCase(status)){
            status = "DISABLE";
        }else{
            status = "ENABLE";
        }

        userService.updateUserStatus(id,status);
        return new ResponseResult(true,200,"响应成功",status);
    }

    //用户登录
    @RequestMapping("/login")
    public ResponseResult login(User user, HttpServletRequest request) throws Exception { //前端请求是get,这里无需使用@RequestBody
        //查询出的user对象
        User user1 = userService.login(user);

        if(user1 != null ){
            //保存用户id及access_token到session中
            HttpSession session = request.getSession();
            String access_token = UUID.randomUUID().toString(); //随机value值
            session.setAttribute("access_token",access_token);
            session.setAttribute("user_id",user1.getId());

            //将查询出的信息响应给前台
            Map<String, Object> map = new HashMap<>();
            map.put("access_token",access_token);
            map.put("user_id",user1.getId());

            //将查询出来的user，也存到map中
            map.put("user",user1);

            return new ResponseResult(true,1,"登录成功",map);
        }else {
            return new ResponseResult(false,400,"用户名密码错误",null);
        }
    }


    //分配角色（回显）
    @RequestMapping("/findUserRoleById")
    public ResponseResult findUserRelationRoleById(Integer id){
        List<Role> roleList = userService.findUserRelationById(id);
        return new ResponseResult(true,200,"分配角色回显成功",roleList);
    }

    //用户分配角色
    @RequestMapping("/userContextRole")
    public ResponseResult userContextRole(@RequestBody UserVO userVO){
        userService.userContextRole(userVO);
        return new ResponseResult(true,200,"分配角色成功",null);
    }

    //获取用户权限，进行菜单动态展示
    @RequestMapping("/getUserPermissions")
    public ResponseResult getUserPermissions(HttpServletRequest request){
        //1.获取请求头中的token
        String header_token = request.getHeader("Authorization");

        //2.获取session中的token
        HttpSession session = request.getSession();
        String session_token = (String) session.getAttribute("access_token");

        //3.判断token是否一致
        if(header_token.equals(session_token)){
            //同一个用户，且是登录状态
            //3.1获取用户id
            Integer userId = (Integer) request.getSession().getAttribute("user_id");

            //3.2调用service,进行菜单信息查询
            ResponseResult responseResult = userService.getUserPermissions(userId);
            return responseResult;
        }else {
            //token不一致
            return new ResponseResult(false,400,"获取菜单失败",null);
        }
    }


}
