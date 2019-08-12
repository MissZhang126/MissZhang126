package net.wanho.mapper;

import net.wanho.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2019/7/30.
 */
public interface UserMapper {
    //    注册 插入用户表
    void register(User user);

    //    插入角色 用户关系表
    int insert(UserRole userRole);

    //    删除用户表
    void deleteUser(Integer id);


    User getUserByName(String userName);

    //    查询角色 通过userid
    User getUserRoles(Integer id);

    //    查询权限 通过roleid
    List<Role> getRolePer(Integer rId);

    //    查询所有用户
    List<User> getAllUser();

    //    查询所有角色
    List<Role> getAllRole();

    //    查询所有权限
    List<Per> getAllPer();

    //    修改用户角色关系表
    void deleteUserRoleRelation(Integer uId);

    void insertUserRoleRelation(@Param("uId") Integer uId, @Param("rId") Integer rId);

    //    修改状态
    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    //    修改角色权限关系
    void deleteRolePerRelation(Integer rId);


    void insertRolePerRelation(@Param("rId") Integer rId, @Param("pId") Integer pId);


    //    新增角色
//    1.新增角色 主键自增长
    void addRole(Role role);
//    2.新增关系表
}
