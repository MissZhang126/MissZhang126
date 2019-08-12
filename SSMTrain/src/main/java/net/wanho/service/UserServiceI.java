package net.wanho.service;

import com.github.pagehelper.PageInfo;
import net.wanho.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserServiceI {
    ResultModel register(User user);


    User getUserByName(String userName);

    //    获取角色 用户信息
    User getUserRoles(Integer id);

    //    获取权限 角色信息
    List<Role> getRolePer(Integer rId);


    //    分页查询所有用户
    PageInfo<User> getAllUserByPage(Integer pageNum);

    //    分页查询所有角色
    PageInfo<Role> getAllRoleByPage(Integer pageNum);

    //    查询所有权限
    List<Per> getAllPer();

    //    查询所有角色
    List<Role> getAllRole();

    //    修改用户角色关系表
    void updateUserRoleRelation(String[] ids, String id);

    //    修改状态
    int updateStatus(Integer id, String status);

    //    修改角色权限关系表
    void updatePerRelation(String[] ids, String id);

    //    新增角色 角色权限关系表
    int addRloe(Role role, String[] pIds);

}
