package net.wanho.service.impl;

import net.wanho.mapper.RoleMapper;
import net.wanho.service.RoleServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/8/6.
 * 角色
 */
@Service
public class RoleServiceImpl implements RoleServiceI {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 删除角色
     * 删除权限角色关系表 两张关系表都需要删 和角色表
     *
     * @param rId 角色rId
     */
//
    @Override
    public void deleteRolePer(Integer rId) {
//        删除角色权限关系表
        roleMapper.deleteRelationRolePer(rId);
//        删除用户角色关系表
        roleMapper.deleteRelationUserRole(rId);
//        最后删除角色表
        roleMapper.deleteRole(rId);
    }


}
