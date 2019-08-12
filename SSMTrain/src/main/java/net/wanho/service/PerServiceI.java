package net.wanho.service;

import net.wanho.pojo.Per;

import java.util.List;

/**
 * Created by Administrator on 2019/8/5.
 */
public interface PerServiceI {
    //    根据id查询
    Per selectPerById(Integer pId);

    //    根据parentId查所有
    List<Per> selectPerByParentId(Integer parentId);

    //    修改权限名称
    int updatePerName(Per per);

    //    删除权限
    void deletePer(Integer pId);

    //    新增权限
    int addPer(Per per);
}
