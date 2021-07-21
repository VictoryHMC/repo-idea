package com.lagou.controller;

import com.lagou.domain.Menu;
import com.lagou.domain.ResponseResult;
import com.lagou.domain.Role;
import com.lagou.domain.RoleMenuVo;
import com.lagou.service.MenuService;
import com.lagou.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //查询所有角色（条件）
    @RequestMapping("/findAllRole")
    public ResponseResult findAllRole(@RequestBody Role role){
        List<Role> roleList = roleService.findAllRole(role);
        return new ResponseResult(true,200,"查询所有角色成功",roleList);
    }

    @Autowired
    private MenuService menuService;

    //查询所有的父子菜单信息（分配菜单的第一个接口）
    @RequestMapping("/findAllMenu")
    public ResponseResult findAllMenu(){
        // -1 表示查询所有的父子级菜单
        List<Menu> menuList = menuService.findSubMenuListByPid(-1);

        //响应数据
        Map<String, Object> map = new HashMap<>();
        map.put("parentMenuList",menuList); //根据接口文档返回对应的json数据

        return new ResponseResult(true,200,"查询所有父子菜单信息成功",map);
    }

    //根据角色id查询该角色关联的菜单信息ID [1,2,3,4]
    @RequestMapping("/findMenuByRoleId")
    public ResponseResult findMenuByRoleId(Integer roleId){
        List<Integer> menuByRoleId = roleService.findMenuByRoleId(roleId);
        return new ResponseResult(true,200,"查询角色关联的菜单信息成功",menuByRoleId);
    }

    //为角色分配菜单
    @RequestMapping("/RoleContextMenu")
    public ResponseResult RoleContextMenu(@RequestBody RoleMenuVo roleMenuVo){
        roleService.roleContextMenu(roleMenuVo);
        return new ResponseResult(true,200,"角色分配菜单（权限）成功",null);
    }

    //删除角色
    @RequestMapping("/deleteRole")
    public ResponseResult deleteRole(Integer id){
        roleService.deleteRole(id);
        return new ResponseResult(true,200,"删除角色成功",null);
    }

}
