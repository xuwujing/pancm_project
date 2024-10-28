package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_ip_alloc")
public class IpAlloc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 建设项目
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 区域
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 建设单位
     */
    private String contractor;

    /**
     * 联系人
     */
    @Column(name = "contractor_person")
    private String contractorPerson;

    /**
     * 联系电话
     */
    @Column(name = "contractor_phone")
    private String contractorPhone;

    /**
     * 分配日期，到天
     */
    @Column(name = "alloc_day")
    private Date allocDay;

    /**
     * 下载显示的文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 服务器文件路径
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private Integer createPerson;

    /**
     * 0,未通过校验；1，已通过校验
     */
    @Column(name = "valid_status")
    private Integer validStatus;

    /**
     * 0,未写入；1，已写入
     */
    @Column(name = "insert_status")
    private Integer insertStatus;


    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取建设项目
     *
     * @return project - 建设项目
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 设置建设项目
     *
     * @param projectName 建设项目
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    /**
     * 获取区域
     *
     * @return area_id - 区域
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置区域
     *
     * @param areaId 区域
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取设备类型
     *
     * @return device_type - 设备类型
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 获取建设单位
     *
     * @return company - 建设单位
     */
    public String getContractor() {
        return contractor;
    }

    /**
     * 设置建设单位
     *
     * @param contractor 建设单位
     */
    public void setContractor(String contractor) {
        this.contractor = contractor == null ? null : contractor.trim();
    }

    /**
     * 获取联系人
     *
     * @return link_person - 联系人
     */
    public String getContractorPerson() {
        return contractorPerson;
    }

    /**
     * 设置联系人
     *
     * @param contractorPerson 联系人
     */
    public void setContractorPerson(String contractorPerson) {
        this.contractorPerson = contractorPerson == null ? null : contractorPerson.trim();
    }

    /**
     * 获取分配日期，到天
     *
     * @return alloc_day - 分配日期，到天
     */
    public Date getAllocDay() {
        return allocDay;
    }

    /**
     * 设置分配日期，到天
     * @param allocDay 分配日期，到天
     */
    public void setAllocDay(Date allocDay) {
        this.allocDay = allocDay;
    }

    /**
     * 获取下载显示的文件名
     *
     * @return file_name - 下载显示的文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置下载显示的文件名
     *
     * @param fileName 下载显示的文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * 获取服务器文件路径
     *
     * @return file_path - 服务器文件路径
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置服务器文件路径
     *
     * @param filePath 服务器文件路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * 获取创建人
     *
     * @return create_person - 创建人
     */
    public Integer getCreatePerson() {
        return createPerson;
    }

    /**
     * 设置创建人
     *
     * @param createPerson 创建人
     */
    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    /**
     * 获取0,未写入；1，已写入
     *
     * @return insertStatus - 0,未写入；1，已写入
     */
    public Integer getInsertStatus() {
        return insertStatus;
    }

    /**
     * 设置0,未写入；1，已写入
     *
     * @param insertStatus 0,未写入；1，已写入
     */
    public void setInsertStatus(Integer insertStatus) {
        this.insertStatus = insertStatus;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public String getContractorPhone() {
        return contractorPhone;
    }

    public void setContractorPhone(String contractorPhone) {
        this.contractorPhone = contractorPhone;
    }

    @Override
    public String toString() {
        return "IpAlloc{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", areaId=" + areaId +
                ", deviceType=" + deviceType +
                ", contractor='" + contractor + '\'' +
                ", contractorPerson='" + contractorPerson + '\'' +
                ", contractorPhone='" + contractorPhone + '\'' +
                ", allocDay=" + allocDay +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", createPerson=" + createPerson +
                ", validStatus=" + validStatus +
                ", insertStatus=" + insertStatus +
                '}';
    }
}