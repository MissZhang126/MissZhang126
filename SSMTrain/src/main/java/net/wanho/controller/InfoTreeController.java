package net.wanho.controller;

import net.sf.json.JSONArray;
import net.wanho.pojo.Per;
import net.wanho.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * zTree
 * 角色页面新增修改 树状图
 */

@Controller
public class InfoTreeController {

    @Autowired
    private UserServiceI userServiceI;

    @RequestMapping(value = "tree", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String tree() throws Exception {
        List<Object> listmap = new ArrayList<Object>();
//        Map<String, Object> itemmap = new HashMap<String, Object>();
//        查询所有权限
        List<Per> result = userServiceI.getAllPer();
//        查询到的权限不为空 则执行
        if (result != null) {
//            匿名迭代器
            for (Iterator iterator = result.iterator(); iterator.hasNext(); ) {
                Per item = (Per) iterator.next();
                Map<String, Object> itemmap = new HashMap<String, Object>();
                itemmap.put("pId", item.getPId());
                itemmap.put("name", item.getPName());
                itemmap.put("parentId", item.getParentId());
                itemmap.put("open", "false");//默认节点展开
                //点击节点后触发的事件。事件具体方式请看js
//                itemmap.put("click", "getInfoId(" + item.getPId() + ")");
                listmap.add(itemmap);
            }
        }
        //这里使用的java自带的json转换器 net.sf.json.JSONArray;
        JSONArray array = JSONArray.fromObject(listmap);
        return array.toString();
    }
}



