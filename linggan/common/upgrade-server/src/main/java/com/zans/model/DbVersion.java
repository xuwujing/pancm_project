package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: (DbVersion)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:33
 */
@Data
@Table(name = "db_version")
public class DbVersion implements Serializable {
    private static final long serialVersionUID = -44629255341757925L;
    /**
     * 自增ID
     */
    @Column(name = "id")
    private Long id;
    /**
     * 版本号
     */
    @Column(name = "version")
    private String version;
    /**
     * 升级说明
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
