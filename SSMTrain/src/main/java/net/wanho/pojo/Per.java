package net.wanho.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/8/1.
 * 权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Per {
    private Integer pId;
    private String pName;
    //    权限父节点id
    private Integer parentId;
    private String url;

}
