package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.vo.BasePage;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (DeviceCipher)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:15:54
 */
@Data
@Table(name = "device_cipher")
public class DeviceCipher extends BasePage implements Serializable {
    private static final long serialVersionUID = 603368489173734845L;
    @Column(name = "id")
    private Integer id;
    @Column(name = "ip")
    private String ip;
    /**
     * 大华、海康、宇视等
     */
    @Column(name = "device_brand")
    private String deviceBrand;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private String deviceType;
    /**
     * 不可修改
     */
    @Column(name = "account")
    private String account;
    /**
     * AES加密
     */
    @Column(name = "password")
    private String password;
    /**
     * 在线状态 0,离线,1,在线
     */
    @Column(name = "alive_status")
    private Integer aliveStatus;
    /**
     * 账号密码有效性
     * 0，不正确，1，正确
     */
    @Column(name = "is_valid")
    private Integer isValid;
    /**
     * 密码强弱 0，弱口令，1，强
     */
    @Column(name = "is_strong")
    private Integer isStrong;
    /**
     * 0，否，1，是
     * (逻辑删除)
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;
    /**
     * 上一次启动时间
     */
    @Column(name = "last_start_time")
    private String lastStartTime;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 更新用户
     */
    @Column(name = "update_user")
    private String updateUser;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
