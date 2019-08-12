package net.wanho.mapper;

/**
 * Created by Administrator on 2019/8/6.
 */
public interface RoleMapper {

    //    删除角色
    void deleteRole(Integer rId);

    //    删除两张关系表
    void deleteRelationRolePer(Integer rId);

    void deleteRelationUserRole(Integer rId);


}
