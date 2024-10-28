package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "radius_server")
public class RadiusServer implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "server_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serverId;

    /**
     * server名称
     */
    @Column(name = "server_name")
    private String serverName;

    /**
     * 数据库类型，mysql|oracle
     */
    @Column(name = "sql_driver")
    private String sqlDriver;

    /**
     * 主机IP
     */
    @Column(name = "sql_host")
    private String sqlHost;

    /**
     * 端口
     */
    @Column(name = "sql_port")
    private Integer sqlPort;

    /**
     * 账号
     */
    @Column(name = "sql_user")
    private String sqlUser;

    /**
     * 密码
     */
    @Column(name = "sql_passwd")
    private String sqlPasswd;

    /**
     * 数据库名称
     */
    @Column(name = "sql_db")
    private String sqlDb;

    /**
     * 是否启用；1.启用；0.禁用
     */
    private Integer enable;

    /**
     * 能否访问；1.启用；0.不可用
     */
    private Integer alive;

    /**
     * 服务IP地址
     */
    @Column(name = "service_host")
    private String serviceHost;

    /**
     * 服务认证端口
     */
    @Column(name = "service_auth_port")
    private Integer serviceAuthPort;

    /**
     * 服务计费端口
     */
    @Column(name = "service_accounting_port")
    private Integer serviceAccountingPort;

    /**
     * 服务密钥
     */
    @Column(name = "service_secret")
    private String serviceSecret;
    /**
     * server描述
     */
    @Column(name = "server_desc")
    private String serverDesc;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return server_id - id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置id
     *
     * @param serverId id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取server名称
     *
     * @return server_name - server名称
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * 设置server名称
     *
     * @param serverName server名称
     */
    public void setServerName(String serverName) {
        this.serverName = serverName == null ? null : serverName.trim();
    }

    /**
     * 获取数据库类型，mysql|oracle
     *
     * @return sql_driver - 数据库类型，mysql|oracle
     */
    public String getSqlDriver() {
        return sqlDriver;
    }

    /**
     * 设置数据库类型，mysql|oracle
     *
     * @param sqlDriver 数据库类型，mysql|oracle
     */
    public void setSqlDriver(String sqlDriver) {
        this.sqlDriver = sqlDriver == null ? null : sqlDriver.trim();
    }

    /**
     * 获取主机IP
     *
     * @return sql_host - 主机IP
     */
    public String getSqlHost() {
        return sqlHost;
    }

    /**
     * 设置主机IP
     *
     * @param sqlHost 主机IP
     */
    public void setSqlHost(String sqlHost) {
        this.sqlHost = sqlHost == null ? null : sqlHost.trim();
    }

    /**
     * 获取端口
     *
     * @return sql_port - 端口
     */
    public Integer getSqlPort() {
        return sqlPort;
    }

    /**
     * 设置端口
     *
     * @param sqlPort 端口
     */
    public void setSqlPort(Integer sqlPort) {
        this.sqlPort = sqlPort;
    }

    /**
     * 获取账号
     *
     * @return sql_user - 账号
     */
    public String getSqlUser() {
        return sqlUser;
    }

    /**
     * 设置账号
     *
     * @param sqlUser 账号
     */
    public void setSqlUser(String sqlUser) {
        this.sqlUser = sqlUser == null ? null : sqlUser.trim();
    }

    /**
     * 获取密码
     *
     * @return sql_passwd - 密码
     */
    public String getSqlPasswd() {
        return sqlPasswd;
    }

    /**
     * 设置密码
     *
     * @param sqlPasswd 密码
     */
    public void setSqlPasswd(String sqlPasswd) {
        this.sqlPasswd = sqlPasswd == null ? null : sqlPasswd.trim();
    }

    /**
     * 获取数据库名称
     *
     * @return sql_db - 数据库名称
     */
    public String getSqlDb() {
        return sqlDb;
    }

    /**
     * 设置数据库名称
     *
     * @param sqlDb 数据库名称
     */
    public void setSqlDb(String sqlDb) {
        this.sqlDb = sqlDb == null ? null : sqlDb.trim();
    }

    /**
     * 获取是否启用；1.启用；0.禁用
     *
     * @return enable - 是否启用；1.启用；0.禁用
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置是否启用；1.启用；0.禁用
     *
     * @param enable 是否启用；1.启用；0.禁用
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * 获取能否访问；1.启用；0.不可用
     *
     * @return alive - 能否访问；1.启用；0.不可用
     */
    public Integer getAlive() {
        return alive;
    }

    /**
     * 设置能否访问；1.启用；0.不可用
     *
     * @param alive 能否访问；1.启用；0.不可用
     */
    public void setAlive(Integer alive) {
        this.alive = alive;
    }

    /**
     * 获取服务IP地址
     *
     * @return service_host - 服务IP地址
     */
    public String getServiceHost() {
        return serviceHost;
    }

    /**
     * 设置服务IP地址
     *
     * @param serviceHost 服务IP地址
     */
    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost == null ? null : serviceHost.trim();
    }

    /**
     * 获取服务认证端口
     *
     * @return service_auth_port - 服务认证端口
     */
    public Integer getServiceAuthPort() {
        return serviceAuthPort;
    }

    /**
     * 设置服务认证端口
     *
     * @param serviceAuthPort 服务认证端口
     */
    public void setServiceAuthPort(Integer serviceAuthPort) {
        this.serviceAuthPort = serviceAuthPort;
    }

    /**
     * 获取服务计费端口
     *
     * @return service_accounting_port - 服务计费端口
     */
    public Integer getServiceAccountingPort() {
        return serviceAccountingPort;
    }

    /**
     * 设置服务计费端口
     *
     * @param serviceAccountingPort 服务计费端口
     */
    public void setServiceAccountingPort(Integer serviceAccountingPort) {
        this.serviceAccountingPort = serviceAccountingPort;
    }

    /**
     * 获取服务密钥
     *
     * @return service_secret - 服务密钥
     */
    public String getServiceSecret() {
        return serviceSecret;
    }

    /**
     * 设置服务密钥
     *
     * @param serviceSecret 服务密钥
     */
    public void setServiceSecret(String serviceSecret) {
        this.serviceSecret = serviceSecret == null ? null : serviceSecret.trim();
    }

    /**
     * 获取server描述
     *
     * @return server_desc - server描述
     */
    public String getServerDesc() {
        return serverDesc;
    }

    /**
     * 设置server描述
     *
     * @param serverDesc server描述
     */
    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc == null ? null : serverDesc.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serverId=").append(serverId);
        sb.append(", serverName=").append(serverName);
        sb.append(", sqlDriver=").append(sqlDriver);
        sb.append(", sqlHost=").append(sqlHost);
        sb.append(", sqlPort=").append(sqlPort);
        sb.append(", sqlUser=").append(sqlUser);
        sb.append(", sqlPasswd=").append(sqlPasswd);
        sb.append(", sqlDb=").append(sqlDb);
        sb.append(", enable=").append(enable);
        sb.append(", alive=").append(alive);
        sb.append(", serviceHost=").append(serviceHost);
        sb.append(", serviceAuthPort=").append(serviceAuthPort);
        sb.append(", serviceAccountingPort=").append(serviceAccountingPort);
        sb.append(", serviceSecret=").append(serviceSecret);
        sb.append(", serverDesc=").append(serverDesc);
        sb.append("]");
        return sb.toString();
    }
}