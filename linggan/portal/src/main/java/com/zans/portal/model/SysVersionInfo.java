package com.zans.portal.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (SysVersionInfo)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:25
 */
@Data
@Table(name = "sys_version_info")
public class SysVersionInfo implements Serializable {
    private static final long serialVersionUID = -88186168665527635L;
    @Column(name = "id")
    private Long id;
    /**
     * 项目名称
     */
    @Column(name = "project_name")
    private String projectName;
    /**
     * 服务所属ip
     */
    @Column(name = "server_ip")
    private String serverIp;
    /**
     * 请求地址
     */
    @Column(name = "server_url")
    private String serverUrl;
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
