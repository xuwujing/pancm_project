package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sys_permission")
public class SysPermission implements Serializable {
    @Id
    @Column(name = "perm_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer permId;

    /**
     * 首页
     */
    @Column(name = "module")
    private Integer module;

    /**
     * 首页
     */
    @Column(name = "perm_name")
    private String permName;


    /**
     * 前端路径，允许为空
     */
    @Column(name = "route")
    private String route;

    /**
     * 接口路径，允许为空
     */
    @Column(name = "api")
    private String api;

    @Column(name = "perm_desc")
    private String permDesc;

    @Column(name = "seq")
    private Integer seq;

    private Integer enable;

    private static final long serialVersionUID = 1L;

    /**
     * @return perm_id
     */
    public Integer getPermId() {
        return permId;
    }

    /**
     * @param permId
     */
    public void setPermId(Integer permId) {
        this.permId = permId;
    }

    /**
     * 获取首页
     *
     * @return perm_name - 首页
     */
    public String getPermName() {
        return permName;
    }

    /**
     * 设置首页
     *
     * @param permName 首页
     */
    public void setPermName(String permName) {
        this.permName = permName == null ? null : permName.trim();
    }


    /**
     * 获取前端路径，允许为空
     *
     * @return ui_uri - 前端路径，允许为空
     */
    public String getRoute() {
        return route;
    }

    /**
     * 设置前端路径，允许为空
     *
     * @param route 前端路径，允许为空
     */
    public void setRoute(String route) {
        this.route = route == null ? null : route.trim();
    }

    /**
     * 获取接口路径，允许为空
     *
     * @return api - 接口路径，允许为空
     */
    public String getApi() {
        return api;
    }

    /**
     * 设置接口路径，允许为空
     *
     * @param api 接口路径，允许为空
     */
    public void setApi(String api) {
        this.api = api == null ? null : api.trim();
    }

    /**
     * @return perm_desc
     */
    public String getPermDesc() {
        return permDesc;
    }

    /**
     * @param permDesc
     */
    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc == null ? null : permDesc.trim();
    }

    public Integer getModule() {
        return module;
    }

    /**
     *
     * @param module
     */
    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
                "permId=" + permId +
                ", module=" + module +
                ", permName='" + permName + '\'' +
                ", route='" + route + '\'' +
                ", api='" + api + '\'' +
                ", permDesc='" + permDesc + '\'' +
                ", seq=" + seq +
                ", enable=" + enable +
                '}';
    }
}