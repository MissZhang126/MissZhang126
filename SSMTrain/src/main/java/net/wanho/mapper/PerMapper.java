package net.wanho.mapper;

import net.wanho.pojo.Per;

import java.util.List;

/**
 * Created by Administrator on 2019/8/5.
 */
public interface PerMapper {

    //    根据id查询
    Per selectPerById(Integer pId);

    //    根据parentId查所有
    List<Per> selectPerByParentId(Integer parentId);

    //    修改权限名称
    void updatePerName(Per per);

    //    删除权限
    void deletePer(Integer pId);

    //    删除关系表
    void deleteRolePer(Integer pId);

    void addPer(Per per);
}
