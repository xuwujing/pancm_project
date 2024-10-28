package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (DeviceCipher)实体类
 *
 * @author beixing
 * @since 2021-08-23 16:15:53
 */
@Data
public class DeviceCipher extends BasePage implements Serializable {
    private static final long serialVersionUID = 888563155071106357L;
    private Integer id;
    private String ip;
    /**
     * 大华、海康、宇视等
     */
    private String deviceBrand;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 不可修改
     */
    private String account;
    /**
     * AES加密
     */
    private String password;
    /**
     * 在线状态 0,离线,1,在线
     */
    private Integer aliveStatus;
    /**
     * 账号密码有效性
     * 0，不正确，1，正确
     */
    private Integer isValid;
    /**
     * 密码强弱 0，弱口令，1，强
     */
    private Integer isStrong;
    /**
     * 0，否，1，是
     * (逻辑删除)
     */
    private Integer deleteStatus;
    /**
     * 上一次启动时间
     */
    private String lastStartTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 更新用户
     */
    private String updateUser;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
