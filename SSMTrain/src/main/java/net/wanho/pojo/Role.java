package net.wanho.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Administrator on 2019/8/1.
 * 角色
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer rId;
    private String rName;
    private List<Per> pers;
}
