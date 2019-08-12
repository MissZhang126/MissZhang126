package net.wanho.realm;

import net.wanho.pojo.*;
import net.wanho.service.UserServiceI;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/**
 * Created by Administrator on 2019/7/30.
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserServiceI userServiceI;

    //    授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        获取页面的用户名
        Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
//                获取角色权限
//                1.获取用户id
        User user1 = userServiceI.getUserByName(primaryPrincipal.toString());

//                2.获取角色信息
        User userRoles = userServiceI.getUserRoles(user1.getId());

//                3.循环获取角色名
        List<String> rolesName = new ArrayList<String>();

//                4.循环获取权限url
        List<String> permissions = new ArrayList<String>();

//            将查出来的rName存到集合中 用于赋予角色
        List<Role> roles = userRoles.getRoles();
        if (roles.size() > 0) {
            for (int j = 0; j < roles.size(); j++) {
                String rName = roles.get(j).getRName();
                rolesName.add(rName);
                List<Role> rolePer = userServiceI.getRolePer(roles.get(j).getRId());
//                多对多 每一个角色对应多个权限 先通过rId获取到pers集合
                if (rolePer.size() > 0) {
                    for (int i = 0; i < rolePer.size(); i++) {
                        List<Per> pers = rolePer.get(i).getPers();
//                    再循环per集合 取出其中的url存入集合中
//                    用于赋予权限
                        if (pers.size() > 0) {
                            for (int p = 0; p < pers.size(); p++) {
                                permissions.add(pers.get(p).getUrl());
                            }
                        }
                    }
                }
            }
        }

//        赋予角色
        if (rolesName.size() > 0) {
            simpleAuthorizationInfo.addRoles(rolesName);
        }

//        赋予权限
        if (permissions.size() > 0) {
            simpleAuthorizationInfo.addStringPermissions(permissions);
        }

        return simpleAuthorizationInfo;


    }

    //    认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        自定义认证
//        获取账号
        String username = (String) token.getPrincipal();
//        获取密码
//        String password = new String((char[])token.getCredentials());

//        通过username获取user
        User user = userServiceI.getUserByName(username);
        if (user == null) {
            throw new RuntimeException("账号或密码有误");
        }
        if ("0".equals(user.getStatus())) {

            throw new RuntimeException("此用户已停用");
        }
        if ("2".equals(user.getStatus())) {

            throw new RuntimeException("此用户已删除");
        }

//      取出用户的密码，按照同样的加密方式进行比对

        return new SimpleAuthenticationInfo(username, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());

    }
}
