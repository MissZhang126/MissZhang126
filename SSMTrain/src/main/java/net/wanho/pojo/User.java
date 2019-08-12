package net.wanho.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Administrator on 2019/7/30.
 * 用户
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String password;
    //    状态 0：停用  1：启用  2：删除状态
    private String status;
    //    盐加密
    private String salt;
    //    角色信息集合
    private List<Role> roles;


}
