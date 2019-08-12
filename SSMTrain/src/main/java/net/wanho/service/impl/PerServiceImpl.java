package net.wanho.service.impl;

import net.wanho.mapper.PerMapper;
import net.wanho.mapper.UserMapper;
import net.wanho.pojo.Per;
import net.wanho.service.PerServiceI;
import net.wanho.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019/8/5.
 * 权限
 */

@Service
public class PerServiceImpl implements PerServiceI {

    @Autowired
    private PerMapper perMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过pId查询所有per
     *
     * @param pId 权限id pId
     * @return
     */
    @Override
    public Per selectPerById(Integer pId) {

        return perMapper.selectPerById(pId);
    }

    /**
     * 通过父ID查询权限 == 查询子权限
     *
     * @param parentId 父id
     * @return
     */
    @Override
    public List<Per> selectPerByParentId(Integer parentId) {

        return perMapper.selectPerByParentId(parentId);
    }

    /**
     * 修改权限名称
     *
     * @param per 权限对象 用于存权限名
     * @return
     */
    @Override
    public int updatePerName(Per per) {
//        页面获取的值不为空才可以修改
        if (per.getPName() != null && !"".equals(per.getPName())) {
            final List<Per> allPer = userMapper.getAllPer();
            if (allPer.size() > 0) {
                for (int i = 0; i < allPer.size(); i++) {
                    if (allPer.get(i).getPName().equals(per.getPName())) {
                        return 0;
                    }
                }
            }
            perMapper.updatePerName(per);
            return 1;
        }
        return 0;

    }

    /**
     * 递归删除权限
     *
     * @param pId 权限id
     */
    @Override
    public void deletePer(Integer pId) {
//        首先将查到的id删除 先删关系表 再删权限表
        perMapper.deleteRolePer(pId);
        perMapper.deletePer(pId);
//        再将id作为parentId查询子权限
        List<Per> pers = perMapper.selectPerByParentId(pId);
        if (pers.size() > 0) {
            for (int i = 0; i < pers.size(); i++) {
//                自己调用自己 循环删除 直到pers.size()==0
                deletePer(pers.get(i).getPId());
            }
        }
    }

    /**
     * 新增权限
     *
     * @param per 权限对象 用于存放名字 url 父级菜单
     * @return
     */
    @Override
    public int addPer(Per per) {
        List<Per> allPer = userMapper.getAllPer();

        for (int i = 0; i < allPer.size(); i++) {
//            循环比较 新增的名字是否已存在
            Per per1 = allPer.get(i);
            if (per1.getPName().equals(per.getPName())) {
//            存在的话返回 0
                return 0;
            }
//            判断url是否存在 存在则不能新增
            if (per1.getUrl().equals(per.getUrl())) {
                return 0;
            }
//            判断是否为最下级菜单 是则不能新增子权限
            if (per1.getPId() == per.getParentId()) {
                for (int j = 0; j < allPer.size(); j++) {
                    if (per1.getParentId() == allPer.get(j).getPId()) {
                        if (allPer.get(j).getParentId() != 0) {
                            return 2;
                        }
                    }
                }
            }
        }

//       以上判断都通过就执行新增
        perMapper.addPer(per);
//        成功返回 1
//        在controller层判断

        return 1;
    }
}
