package net.wanho.controller;

import net.wanho.pojo.Per;
import net.wanho.pojo.ResultModel;
import net.wanho.pojo.Role;
import net.wanho.service.PerServiceI;
import net.wanho.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限controller
 */

@Controller
public class PerController {

    @Autowired
    private PerServiceI perServiceI;
    @Autowired
    private UserServiceI userServiceI;

    ResultModel resultModel = new ResultModel();

    /**
     * 查询所有菜单
     *
     * @param map
     * @return
     */
    @RequestMapping("/getPer")
    public String getPer(Map map) {
        List<Per> allPer = userServiceI.getAllPer();
//        等级

        map.put("allPer", allPer);
        return "security";
    }

    /**
     * 一级菜单
     *
     * @param map
     * @return 权限菜单页面
     */
    @RequestMapping("perFirst")
    public String getPer1(Map map) {
        List<Per> allPer = userServiceI.getAllPer();
        List<Per> firstP = new ArrayList<Per>();

//        第一层级
        for (int i = 0; i < allPer.size(); i++) {
            if (allPer.get(i).getParentId() == 0) {
                firstP.add(allPer.get(i));
            }
        }
        map.put("firstP", firstP);

        return "security";
    }

    /**
     * 二级菜单 三级菜单
     *
     * @param pId 权限id
     * @return resultModel
     */
    @RequestMapping("perSecond")
    @ResponseBody
    public ResultModel getPer2(Integer pId) {
//          通过传过来的PId 将其作为parentId查询子菜单
        List<Per> secondP = perServiceI.selectPerByParentId(pId);
//        存到结果集
        resultModel.setData(secondP);
        return resultModel;
    }

    /**
     * 回填权限名称
     *
     * @param pId 权限id
     * @return resultModel
     */
    @RequestMapping("messagePer")
    @ResponseBody
    public ResultModel getPerName(Integer pId) {
//        将获取到的pId事先存入结果集中 修改时使用
        resultModel.setResultCode(pId);
//        根据pId查询到对应权限对象
        Per per = perServiceI.selectPerById(pId);
//        获取权限名
        String pName = per.getPName();
//        存入结果集
        resultModel.setMsg(pName);
        return resultModel;

    }

    /**
     * 修改权限名称
     *
     * @param pName 权限名称
     * @return 查询一级菜单方法
     */
    @RequestMapping("updatePer")
    @ResponseBody
    public ResultModel updatePer(String pName/*,String id*/) {
//        获取回填时的权限ID
        Integer pId = resultModel.getResultCode();
        Per per = new Per();

        per.setPId(pId);
       /* if ("x".equals(id)){
            per.setParentId(0);
        }else {
            per.setParentId(Integer.parseInt(id));
        }*/
        per.setPName(pName);
//        修改权限名称
        int i = perServiceI.updatePerName(per);
        if (i == 0) {
            resultModel.setMsg("修改失败,权限名重复");
        } else {
            resultModel.setMsg("修改成功");
        }
//        return "redirect:perFirst";
        return resultModel;
    }

    /**
     * 递归删除权限
     *
     * @param pId 权限id
     * @return resultModel
     */
    @RequestMapping("deletePer")
    @ResponseBody
    public ResultModel deletePer(Integer pId) {
        perServiceI.deletePer(pId);
        resultModel.setMsg("删除成功！");

        return resultModel;
    }


    /**
     * 新增
     *
     * @param pName
     * @param id
     * @param url
     * @return
     */
    @RequestMapping("/addPer")
    @ResponseBody
    public ResultModel addPer(String pName, String id, String url) {
//        一次只能新增一个
        Per per = new Per();
        if (pName == null || "".equals(pName)) {
            resultModel.setMsg("权限名称不能为空");
            return resultModel;
        }
        per.setPName(pName);

//        如果有选中id 则添加
        if (id != null && !"".equals(id) && !"x".equals(id)) {
            per.setParentId(Integer.parseInt(id));
        } else {
//            没有选中 默认为根目录
            per.setParentId(0);
        }
        if (url == null || "".equals(url)) {
            resultModel.setMsg("权限url不能为空");
            return resultModel;
        }
        per.setUrl(url);
//        新增权限 为1 成功
        int i = perServiceI.addPer(per);
        if (i == 0) {
            resultModel.setMsg("新增失败，该权限名称已存在");
            return resultModel;
        }
        if (i == 2) {
            resultModel.setMsg("新增失败，无法添加四级权限");
            return resultModel;
        }
//        i == 1
        resultModel.setMsg("新增成功");
        return resultModel;
    }


}