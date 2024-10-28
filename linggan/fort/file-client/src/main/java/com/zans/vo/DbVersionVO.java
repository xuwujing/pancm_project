package com.zans.vo;

import com.alibaba.fastjson.JSONObject;


import java.io.Serializable;

/**
 * @author beixing
 * @Title: (DbVersion)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-24 17:27:40
 */

public class DbVersionVO implements Serializable {
    private static final long serialVersionUID = -54443014638479337L;
    /**
     * 自增ID
     */

    private Long id;
    /**
     * 版本号
     */

    private String version;
    /**
     * 升级说明
     */

    private String remark;
    /**
     * 创建时间
     */

    private String createTime;
    /**
     * 更新时间
     */

    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
