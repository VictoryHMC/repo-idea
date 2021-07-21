package com.lagou.service;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //用户分页&多条件查询
    public PageInfo findAllUserByPage(UserVO userVO);

    //用户状态设置（是否禁用）
    public void updateUserStatus(int id,String status);

    //用户登录
    public User login(User user) throws Exception;

    //分配角色（回显）
    public List<Role> findUserRelationById(Integer id);

    //用户关联角色
    public void userContextRole(UserVO userVO);

    //获取用户权限，进行菜单动态展示
    public ResponseResult getUserPermissions(Integer userId);

}
