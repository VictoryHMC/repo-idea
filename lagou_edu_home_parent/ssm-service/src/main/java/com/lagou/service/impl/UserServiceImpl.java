package com.lagou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lagou.dao.UserMapper;
import com.lagou.domain.*;
import com.lagou.service.UserService;
import com.lagou.utils.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo findAllUserByPage(UserVO userVO) {
        PageHelper.startPage(userVO.getCurrentPage(),userVO.getPageSize());
        
        List<User> userList = userMapper.findAllUserByPage(userVO);

        //有参构造注入，返回分页对象
        PageInfo<User> userPageInfo = new PageInfo<>(userList);

        return userPageInfo;
    }

    @Override
    public void updateUserStatus(int id, String status) {
        userMapper.updateUserStatus(id,status);
    }

    //用户登录
    @Override
    public User login(User user) throws Exception {
        //调用mapper方法 user2:包含密文密码
        User user2 = userMapper.login(user);

        //对象不为空，且输入的密码加密钥等于数据库的密文密码
        //                                  输入的密码       密钥（盐）     密文密码
        if(user2 != null && Md5.verify(user.getPassword(),"lagou",user2.getPassword())){
            return user2;
        }else {
            return null;
        }
    }

    //分配角色回显
    @Override
    public List<Role> findUserRelationById(Integer id) {
        return userMapper.findUserRelationRoleById(id);
    }

    //用户分配角色
    @Override
    public void userContextRole(UserVO userVO) {
        //1.根据用户id清空中间表关联关系
        userMapper.deleteUserContextRole(userVO.getUserId());
        
        //2.重新建立关联关系
        for (Integer roleId : userVO.getRoleIdList()) {
            //封装数据
            User_Role_relation user_role_relation = new User_Role_relation();
            user_role_relation.setUserId(userVO.getUserId());
            user_role_relation.setRoleId(roleId);

            Date date = new Date();
            user_role_relation.setCreatedTime(date);
            user_role_relation.setUpdatedTime(date);

            user_role_relation.setCreatedBy("system");//创建人
            user_role_relation.setUpdatedby("system");//更新人

            userMapper.userContextRole(user_role_relation);
        }
    }

    //获取用户权限信息
    @Override
    public ResponseResult getUserPermissions(Integer userId) {
        //1.获取当前用户拥有的角色
        List<Role> roleList = userMapper.findUserRelationRoleById(userId);

        //2.获取角色id，保存到List集合中
        ArrayList<Integer> roleIds = new ArrayList<>();
        
        for (Role role : roleList) {
            roleIds.add(role.getId());
        }
        
        //3.根据角色ID查询父菜单
        List<Menu> parentMenu = userMapper.findParentMenuByRoleId(roleIds);

        //4.查询封装父菜单关联的子菜单
        for (Menu menu : parentMenu) {
            List<Menu> subMenu = userMapper.findSubMenuByPid(menu.getId());
            menu.setSubMenList(subMenu);
        }

        //5.获取资源信息
        List<Resource> resourceList = userMapper.findResourceByRoleId(roleIds);

        //6.封装数据并返回（查看接口文档）
        Map<Object, Object> map = new HashMap<>();
        map.put("menuList",parentMenu);
        map.put("resourceList",resourceList);

        return new ResponseResult(true,200,"获取用户权限信息成功",map);
    }
}
