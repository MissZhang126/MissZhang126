package net.wanho.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.wanho.mapper.UserMapper;
import net.wanho.pojo.*;
import net.wanho.service.UserServiceI;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 角色
 */
@Service
@Transactional
public class UserServiceImpl implements UserServiceI {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     * 判断有重复的用户名 如果状态为启用或禁用 则不允许注册
     * 如果用户名重复 但已存在的用户为逻辑删除的状态 就可以注册 并且将原本逻辑删的用户真删
     * 无重复则直接注册 密码加密 跳转登录页面
     *
     * @param user
     * @return
     */
    @Override
    public ResultModel register(User user) {

        ResultModel resultModel = new ResultModel();
        resultModel.setMsg("1");
//        密码加密
        if (user.getUserName() == null || "".equals(user.getUserName())) {
            throw new RuntimeException("参数不能为空");

        }
        if (user.getPassword() == null || "".equals(user.getPassword())) {
            throw new RuntimeException("参数不能为空");

        }
        List<User> allUser = userMapper.getAllUser();
//        判断用户名是否已被注册 并且不是已被删除的用户
        for (int i = 0; i < allUser.size(); i++) {
            if (allUser.get(i).getUserName().equals(user.getUserName()) && !allUser.get(i).getStatus().equals("2")) {
                throw new RuntimeException("已有此用户名");

            }
        }
//        判断如果该用户名已存在 但之前的用户已经被逻辑删除 则将原来的已被删除的用户真删
        for (int i = 0; i < allUser.size(); i++) {
            if (allUser.get(i).getUserName().equals(user.getUserName()) && allUser.get(i).getStatus().equals("2")) {
//                删除用户角色关系表
                userMapper.deleteUserRoleRelation(allUser.get(i).getId());
//                删除用户
                userMapper.deleteUser(allUser.get(i).getId());
            }
        }

        user.setStatus("1");
//        获取盐值
        String salt = UUID.randomUUID().toString();
//        存盐值
        user.setSalt(salt);
//        存密码
        user.setPassword(shiroMD5(user.getPassword(), salt));

//        数据插入用户表
        userMapper.register(user);
//        获取user的主键id
        Integer id = user.getId();
        UserRole userRole = new UserRole();
//        给用户默认角色为1 普通用户
        List<Role> allRole = userMapper.getAllRole();
        if (allRole.size() > 0 && !"".equals(allRole)) {
            for (int i = 0; i < allRole.size(); i++) {
                if (allRole.get(i).getRId() == 1) {
                    userRole.setUId(id);
                    userRole.setRId(1);
                    userMapper.insert(userRole);
                }
            }
        }

        return resultModel;

    }

    /**
     * 通过用户名查询用户信息==>用在登录时获取用户id
     *
     * @param userName
     * @return
     */
    @Override
    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    /**
     * 通过userId查询用户角色关系表信息
     *
     * @param id 用户id
     * @return
     */
    @Override
    public User getUserRoles(Integer id) {


        return userMapper.getUserRoles(id);
    }

    /**
     * 通过角色id查询角色权限信息
     *
     * @param rId 角色id
     * @return
     */
    @Override
    public List<Role> getRolePer(Integer rId) {

        return userMapper.getRolePer(rId);
    }

    /**
     * 分页查询用户信息
     *
     * @param pageNum 页码
     * @return
     */
    @Override
    public PageInfo<User> getAllUserByPage(Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        List<User> allUser = userMapper.getAllUser();
        PageInfo<User> pageInfo = new PageInfo<User>(allUser);
        return pageInfo;
    }

    /**
     * 分页查询角色信息
     *
     * @param pageNum 页码
     * @return
     */
    @Override
    public PageInfo<Role> getAllRoleByPage(Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        List<Role> allRole = userMapper.getAllRole();
        PageInfo<Role> pageInfo = new PageInfo<Role>(allRole);
        return pageInfo;
    }

    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public List<Per> getAllPer() {

        return userMapper.getAllPer();
    }

    /**
     * 查询所有角色信息
     *
     * @return
     */
    @Override
    public List<Role> getAllRole() {
        return userMapper.getAllRole();
    }


    /**
     * 修改用户角色
     *
     * @param ids 角色id的数组
     * @param id  用户id
     */
    @Override
    public void updateUserRoleRelation(String[] ids, String id) {
//        通过用户id删除用户角色关系表
        userMapper.deleteUserRoleRelation(Integer.parseInt(id));
        for (int i = 0; i < ids.length; i++) {
//            循环插入关系表数据
            userMapper.insertUserRoleRelation(Integer.parseInt(id), Integer.parseInt(ids[i]));
        }
    }

    /**
     * 修改用户状态 包括禁用和删除
     *
     * @param id     用户id
     * @param status 用户状态 0 1 2
     * @return
     */
    @Override
    public int updateStatus(Integer id, String status) {
        int i = userMapper.updateStatus(id, status);
        return i;
    }

    /**
     * 修改角色表 权限信息
     *
     * @param ids 权限id数组
     * @param rId 当前角色id
     */
    @Override
    public void updatePerRelation(String[] ids, String rId) {
        userMapper.deleteRolePerRelation(Integer.parseInt(rId));
        for (int i = 0; i < ids.length; i++) {
            userMapper.insertRolePerRelation(Integer.parseInt(rId), Integer.parseInt(ids[i]));
        }
    }

    /**
     * 新增角色
     *
     * @param role 对象 存储页面获取的姓名
     * @param pIds 权限id的数组
     * @return
     */
    @Override
    public int addRloe(Role role, String[] pIds) {
//        获取所有角色 以便判断是否有重复
        List<Role> allRole = userMapper.getAllRole();
        for (int i = 0; i < allRole.size(); i++) {
//            循环比较 新增的名字是否已存在
            if (allRole.get(i).getRName().equals(role.getRName())) {
//            存在的话返回 0
                return 0;
            }
        }
//        没有重复的就执行新增
        userMapper.addRole(role);
//        循环新增角色权限关系表
        for (int i = 0; i < pIds.length; i++) {
            userMapper.insertRolePerRelation(role.getRId(), Integer.parseInt(pIds[i]));
        }
//        成功返回 1
//        在controller层判断
        return 1;

    }


    public String shiroMD5(String password, String salt) {
//        加密方式 与配置中名称保持一致
        String hashAlgorithmName = "Md5";
//        加密次数 与配置中名称保持一致
        int hashIterations = 2;
//        把salt转成ByteSource
        ByteSource saltSource = ByteSource.Util.bytes(salt);

        Object object = new SimpleHash(hashAlgorithmName, password, saltSource, hashIterations);
        return object.toString();


    }

}
