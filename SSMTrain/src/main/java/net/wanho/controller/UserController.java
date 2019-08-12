package net.wanho.controller;

import com.github.pagehelper.PageInfo;
import net.wanho.pojo.ResultModel;
import net.wanho.pojo.Role;
import net.wanho.pojo.User;
import net.wanho.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 用户controller
 */
@Controller
public class UserController {
    @Autowired
    private UserServiceI userServiceI;

    ResultModel resultModel = new ResultModel();

    /**
     * 用户页面 回填 查询角色
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping("/getAllRoleById")
    @ResponseBody
    public ResultModel getAllRoleById(Integer id) {
//        通过用户id查询角色信息
        User userRoles = userServiceI.getUserRoles(id);
        if (userRoles != null && !"".equals(userRoles)) {
            List<Role> roles = userRoles.getRoles();
//            判断该用户是否有对应角色 有的话则存到结果集中返回
//            如果将空值传到ajax 会导致空指针
            if (roles.size() > 0) {
                resultModel.setData(roles);
            }
        }

//        将id存起来
        resultModel.setMsg(id + "");
        return resultModel;

    }

    /**
     * 用户页面 角色修改
     *
     * @param ids 角色id数组
     * @return
     */
    @RequestMapping("/updateRole")
    @ResponseBody
    public ResultModel updateRole(String[] ids) {
//        获取回写时存入的用户id
        String msg = resultModel.getMsg();
//        判断是否勾选的值
        if (ids == null) {
            resultModel.setMsg("修改失败 不能为空值");
            return resultModel;
        }
        userServiceI.updateUserRoleRelation(ids, msg);
        resultModel.setMsg("修改成功");
        return resultModel;
    }

    /**
     * 分页查询所有用户
     *
     * @param pageNum 页码
     * @param map     集合 赋值
     * @return
     */
    @RequestMapping("/getUser")
    public String getUser(@RequestParam(defaultValue = "1") Integer pageNum, Map map) {
        PageInfo<User> pageInfo = userServiceI.getAllUserByPage(pageNum);
//        先查出所有角色信息存入模态框
        List<Role> allRole = userServiceI.getAllRole();
        map.put("list", allRole);
        map.put("pageInfo", pageInfo);
        return "userList";
    }


    /**
     * 修改状态 禁用或删除
     *
     * @param id     用户id
     * @param status 状态
     * @return
     */
    @RequestMapping("/changeStatus/{id}/{choose}")
    public String changeStatus(@PathVariable("id") Integer id, @PathVariable("choose") String status) {

        userServiceI.updateStatus(id, status);

        return "redirect:/getUser";
    }

}
