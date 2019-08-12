package net.wanho.controller;

import com.github.pagehelper.PageInfo;
import net.wanho.pojo.Per;
import net.wanho.pojo.ResultModel;
import net.wanho.pojo.Role;
import net.wanho.service.RoleServiceI;
import net.wanho.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色controller
 */
@Controller
public class RoleController {
    @Autowired
    private RoleServiceI roleServiceI;
    @Autowired
    private UserServiceI userServiceI;

    ResultModel resultModel = new ResultModel();

    /**
     * 分页查询所有角色
     *
     * @param pageNum 页码
     * @param map     键值对 传值到页面
     * @return
     */
    @RequestMapping("/getRole")
    public String getRole(@RequestParam(defaultValue = "1") Integer pageNum, Map map) {
        PageInfo<Role> pageInfo = userServiceI.getAllRoleByPage(pageNum);
        map.put("pageInfo", pageInfo);
//        事先将所有权限查出来 放入模态框中
        List<Per> allPer = userServiceI.getAllPer();
        map.put("allPer", allPer);
        return "roleList";
    }

    /**
     * 角色表 回填 查询选中菜单
     *
     * @param rId 角色id
     * @return
     */
    @RequestMapping("/getAllPer")
    @ResponseBody
    public ResultModel getAllPer(Integer rId) {
//        通过角色id查询 关系表 role的实体类中包括pers权限集合
        List<Role> rolePer = userServiceI.getRolePer(rId);
        List<Per> pers = new ArrayList<Per>();
//        有值的情况下 将权限存到权限集合中
//        由于修改时每次只能选一个角色 因此使用get(0)
        if (rolePer.size() > 0) {
            pers = rolePer.get(0).getPers();
//        等级
            if (pers.size() > 0) {
                resultModel.setData(pers);
            }
        }
//        存roleId 便于修改时取值
        resultModel.setMsg(rId + "");
        return resultModel;
    }

    /**
     * 角色表 修改权限
     *
     * @param ids ajax传来的权限id数组
     * @return
     */
    @RequestMapping("/updatePerRole")
    @ResponseBody
    public ResultModel updatePer(String[] ids) {
//        回填时存的值
        String msg = resultModel.getMsg();
//        判断修改时是否有选 没有选择不能修改
        if (ids == null || ids.length <= 0 || "".equals(ids)) {
            resultModel.setMsg("修改失败 不能为空值");
        } else {
            userServiceI.updatePerRelation(ids, msg);
            resultModel.setMsg("修改成功");
        }
        return resultModel;
    }

    /**
     * 新增角色 包括角色名和权限
     *
     * @param rNames ajax传的角色名
     * @param ids    选中的权限id集合
     * @return
     */
    @RequestMapping("/addRole")
    @ResponseBody
    public ResultModel addRole(String[] rNames, String[] ids) {
//        一次只能新增一个
        Role role = new Role();
        String rName = rNames[0];
        role.setRName(rName);
//        调用新增方法 同时加入两张表 角色表和角色权限关系表
        int i = userServiceI.addRloe(role, ids);
        if (i == 0) {
            resultModel.setMsg("新增失败，该角色已存在");
            return resultModel;
        }
        resultModel.setMsg("新增成功");
        return resultModel;
    }

    /**
     * 删除角色 真删 包括关系表全部删除
     *
     * @param rId 角色id
     * @return
     */
    @RequestMapping("/deleteRole/{rId}")
    public String deleteRole(@PathVariable("rId") Integer rId) {
        roleServiceI.deleteRolePer(rId);
        return "redirect:/getRole";
    }

}
