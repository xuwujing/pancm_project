package com.zans.mms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "sys_version_info")
public class SysVersionInfo implements Serializable {
    
    @Column(name = "id")
    private Integer id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_desc")
    private String projectDesc;

    @Column(name = "server_ip")
    private String serverIp;

    @Column(name = "server_url")
    private String serverUrl;

    @Column(name = "verify_success")
    private Integer verifySuccess;

    @Column(name = "update_time")
    private String updateTime;


    @Column(name = "service_account")
    private String serviceAccount;

    @Column(name = "service_password")
    private String servicePassword;

    @Column(name = "service_port")
    private String servicePort;


}
