package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "radius_endpoint")
public class RadiusEndpoint implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 终端mac地址，17位
     */
    private String mac;



    @Column(name = "base_ip")
    private String baseIp;

    /**
     * 审批人
     */
    @Column(name = "auth_person")
    private String authPerson;

    /**
     * 审核意见
     */
    @Column(name = "auth_mark")
    private String authMark;

    /**
     * 审核时间
     */
    @Column(name = "auth_time")
    private Date authTime;

    /**
     * 0, 阻断；1，检疫区，2，放行
     */
    @Column(name = "access_policy")
    private Integer accessPolicy;

    /**
     * 1, Radius写入；2，系统批量导入，3，前台写入
     */
    @Column(name = "insert_type")
    private Integer insertType;

    /**
     * 0, 正常； 1，删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    /**
     *  0，系统;1,手工添加；2，导入
     */
    @Column(name = "source")
    private Integer source;

    /**
     * 创建人，默认admin
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 终端mac地址
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }



    /**
     * 获取审批人
     *
     * @return auth_person - 审批人
     */
    public String getAuthPerson() {
        return authPerson;
    }

    /**
     * 设置审批人
     *
     * @param authPerson 审批人
     */
    public void setAuthPerson(String authPerson) {
        this.authPerson = authPerson == null ? null : authPerson.trim();
    }

    /**
     * 获取审核意见
     *
     * @return auth_mark - 审核意见
     */
    public String getAuthMark() {
        return authMark;
    }

    /**
     * 设置审核意见
     *
     * @param authMark 审核意见
     */
    public void setAuthMark(String authMark) {
        this.authMark = authMark == null ? null : authMark.trim();
    }

    /**
     * 获取审核时间
     *
     * @return auth_time - 审核时间
     */
    public Date getAuthTime() {
        return authTime;
    }

    /**
     * 设置审核时间
     *
     * @param authTime 审核时间
     */
    public void setAuthTime(Date authTime) {
        this.authTime = authTime;
    }

    /**
     * 获取0, 阻断；1，检疫区，2，放行
     *
     * @return access_policy - 0, 阻断；1，检疫区，2，放行
     */
    public Integer getAccessPolicy() {
        return accessPolicy;
    }

    /**
     * 设置0, 阻断；1，检疫区，2，放行
     *
     * @param accessPolicy 0, 阻断；1，检疫区，2，放行
     */
    public void setAccessPolicy(Integer accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    /**
     * 获取1, Radius写入；2，系统批量导入，3，前台写入
     *
     * @return insert_type - 1, Radius写入；2，系统批量导入，3，前台写入
     */
    public Integer getInsertType() {
        return insertType;
    }

    /**
     * 设置1, Radius写入；2，系统批量导入，3，前台写入
     *
     * @param insertType 1, Radius写入；2，系统批量导入，3，前台写入
     */
    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    /**
     * 获取0, 正常； 1，删除
     *
     * @return delete_status - 0, 正常； 1，删除
     */
    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置0, 正常； 1，删除
     *
     * @param deleteStatus 0, 正常； 1，删除
     */
    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }



    /**
     * 获取终端mac地址
     *
     * @return remark - 终端mac地址
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置终端mac地址
     *
     * @param remark 终端mac地址
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mac=").append(mac);
        sb.append(", authPerson=").append(authPerson);
        sb.append(", authMark=").append(authMark);
        sb.append(", authTime=").append(authTime);
        sb.append(", accessPolicy=").append(accessPolicy);
        sb.append(", insertType=").append(insertType);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", remark=").append(remark);
        sb.append(", source=").append(source);
        sb.append(", createPerson=").append(createPerson);
        sb.append("]");
        return sb.toString();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getBaseIp() {
        return baseIp;
    }

    public void setBaseIp(String baseIp) {
        this.baseIp = baseIp;
    }
}
