package net.wanho.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 2019/8/1.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultModel {
    //返回值 0成功 1失败
    private Integer resultCode;
    //返回的数据
    private Object data;
    //返回的信息
    private String msg;
}
