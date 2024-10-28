package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (FortUserFile)实体类
 *
 * @author beixing
 * @since 2021-08-17 17:45:15
 */
@Data
public class FortUserFileVO implements Serializable {
    private static final long serialVersionUID = -16756976225699908L;
    private Long id;
    private String userName;
    /**
     * 用户的文件路径(linux)
     */
    private String url;
    /**
     * 映射的盘符名称
     */
    private String driveName;
    /**
     * Windows桌面快捷方式的地址
     */
    private String linkName;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
