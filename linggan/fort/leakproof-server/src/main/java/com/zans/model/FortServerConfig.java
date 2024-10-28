package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (FortServerConfig)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-28 14:17:38
 */
@Data
@Table(name = "fort_server_config")
public class FortServerConfig implements Serializable {
    private static final long serialVersionUID = -10518904506957086L;
    /**
     * 自增ID
     */
    @Column(name = "server_id")
    private Integer serverId;
    /**
     * 主机名称
     */
    @Column(name = "server_name")
    private String serverName;
    /**
     * 应用名称
     */
    @Column(name = "server_application")
    private String serverApplication;
    /**
     * ip
     */
    @Column(name = "server_ip")
    private String serverIp;
    /**
     * 端口
     */
    @Column(name = "server_port")
    private String serverPort;
    /**
     * 账号
     */
    @Column(name = "server_account")
    private String serverAccount;
    /**
     * 密码
     */
    @Column(name = "server_pwd")
    private String serverPwd;
    /**
     * 启用状态0否，1是
     */
    @Column(name = "server_enable")
    private Integer serverEnable;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
