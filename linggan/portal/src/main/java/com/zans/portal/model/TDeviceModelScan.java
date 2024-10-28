package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "t_device_model_scan")
public class TDeviceModelScan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 公司名
     */
    private String company;

    /**
     * 开放端口
     */
    @Column(name = "open_port")
    private String openPort;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 具体型号
     */
    @Column(name = "device_des")
    private String deviceDes;

    /**
     * 是否哑终端 0:不是哑终端 1:哑终端
     */
    @Column(name = "mute")
    private Integer mute;

    /**
     * model来源.0:手工添加；1:onvif扫描
     */
    @Column(name = "insert_source")
    private Integer insertSource;

    /**
     * 该模型是从哪个IP地址学习来的
     */
    @Column(name = "insert_ip")
    private String insertIp;

    /**
     * 模型学习时间
     */
    @Column(name = "insert_time")
    private Date insertTime;

    /**
     * 0:未确认；1：确认
     */
    private Integer confirm;

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
     * 获取公司名
     *
     * @return company - 公司名
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置公司名
     *
     * @param company 公司名
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取开放端口
     *
     * @return open_port - 开放端口
     */
    public String getOpenPort() {
        return openPort;
    }

    /**
     * 设置开放端口
     *
     * @param openPort 开放端口
     */
    public void setOpenPort(String openPort) {
        this.openPort = openPort == null ? null : openPort.trim();
    }

    /**
     * 获取设备类型
     *
     * @return device_type_id - 设备类型
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
     * 获取具体型号
     *
     * @return device_des - 具体型号
     */
    public String getDeviceDes() {
        return deviceDes;
    }

    /**
     * 设置具体型号
     *
     * @param deviceDes 具体型号
     */
    public void setDeviceDes(String deviceDes) {
        this.deviceDes = deviceDes == null ? null : deviceDes.trim();
    }

    /**
     * 获取是否哑终端 0:不是哑终端 1:哑终端
     *
     * @return is_mute - 是否哑终端 0:不是哑终端 1:哑终端
     */
    public Integer getMute() {
        return mute;
    }

    /**
     * 设置是否哑终端 0:不是哑终端 1:哑终端
     *
     * @param mute 是否哑终端 0:不是哑终端 1:哑终端
     */
    public void setMute(Integer mute) {
        this.mute = mute;
    }

    /**
     * 获取model来源.0:手工添加；1:onvif扫描
     *
     * @return insert_source - model来源.0:手工添加；1:onvif扫描
     */
    public Integer getInsertSource() {
        return insertSource;
    }

    /**
     * 设置model来源.0:手工添加；1:onvif扫描
     *
     * @param insertSource model来源.0:手工添加；1:onvif扫描
     */
    public void setInsertSource(Integer insertSource) {
        this.insertSource = insertSource;
    }

    /**
     * 获取该模型是从哪个IP地址学习来的
     *
     * @return insert_ip - 该模型是从哪个IP地址学习来的
     */
    public String getInsertIp() {
        return insertIp;
    }

    /**
     * 设置该模型是从哪个IP地址学习来的
     *
     * @param insertIp 该模型是从哪个IP地址学习来的
     */
    public void setInsertIp(String insertIp) {
        this.insertIp = insertIp == null ? null : insertIp.trim();
    }

    /**
     * 获取模型学习时间
     *
     * @return insert_time - 模型学习时间
     */
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * 设置模型学习时间
     *
     * @param insertTime 模型学习时间
     */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * 获取0:未确认；1：确认
     *
     * @return confirm - 0:未确认；1：确认
     */
    public Integer getConfirm() {
        return confirm;
    }

    /**
     * 设置0:未确认；1：确认
     *
     * @param confirm 0:未确认；1：确认
     */
    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", company=").append(company);
        sb.append(", openPort=").append(openPort);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", deviceDes=").append(deviceDes);
        sb.append(", isMute=").append(mute);
        sb.append(", insertSource=").append(insertSource);
        sb.append(", insertIp=").append(insertIp);
        sb.append(", insertTime=").append(insertTime);
        sb.append(", confirm=").append(confirm);
        sb.append("]");
        return sb.toString();
    }
}