package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (FortUserFile)实体类
 *
 * @author beixing
 * @since 2021-08-17 17:45:15
 */
@Data
@Table(name = "fort_user_file")
public class FortUserFile implements Serializable {
    private static final long serialVersionUID = 623125156808751454L;
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name")
    private String userName;
    /**
     * 用户的文件路径(linux)
     */
    @Column(name = "url")
    private String url;

    @Column(name = "ip")
    private String ip;
    /**
     * 映射的盘符名称
     */
    @Column(name = "drive_name")
    private String driveName;
    /**
     * Windows桌面快捷方式的地址
     */
    @Column(name = "link_name")
    private String linkName;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
