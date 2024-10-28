package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.vo.excel.ExcelProperty;
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
public class DeviceCipherExcel extends BasePage implements Serializable {
    private static final long serialVersionUID = 603368489173734845L;
    @ExcelProperty(ignore = true)
    private Integer id;
    @ExcelProperty(value = "IP地址", index = 1, width = 15)
    private String ip;
    /**
     * 大华、海康、宇视等
     */
    @ExcelProperty(value = "设备", index = 2, width = 10)
    private String deviceBrand;
    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型", index = 3, width = 10)
    private String deviceType;
    /**
     * 不可修改
     */
    @ExcelProperty(value = "账号", index = 4, width = 15)
    private String account;
    /**
     * AES加密
     */
    @ExcelProperty(value = "加密密码", index = 5, width = 15)
    private String password;

    /**
     * aliveStatus = 1 | 1, 在线 不设置
     * aliveStatus = 0， 设置红色
     */
    @ExcelProperty(ignore = true)
    private Integer aliveStatus;
    @ExcelProperty(value = "在线状态", index = 6, colors = {"在线", "", "离线", "red"})
    private String aliveName;
    /**
     * 账号密码有效性
     * 0，不正确，1，正确
     */
    private Integer isValid;
    @ExcelProperty(value = "密码有效性", index = 7, colors = {"正确", "", "不正确", "red"}, width = 15)
    private String isValidName;
    /**
     * 密码强弱 0，弱口令，1，强
     */
    @Column(name = "is_strong")
    private Integer isStrong;

    @ExcelProperty(value = "密码强弱", index = 8, colors = {"强口令", "", "弱口令", "red"})
    private String isStrongName;
    /**
     * 0，否，1，是
     * (逻辑删除)
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    @ExcelProperty(value = "是否删除", index = 9, colors = {"未删除", "", "已删除", "red"})
    private String deleteStatusName;

    /**
     * 上一次启动时间
     */
    @ExcelProperty(value = "上一次启动时间", index = 10, width = 15)
    private String lastStartTime;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 11)
    private String remark;

    /**
     * 更新用户
     */
    @ExcelProperty(value = "更新用户", index = 12)
    private String updateUser;

    @ExcelProperty(value = "创建时间", index = 13, width = 15)
    private String createTime;

    @ExcelProperty(value = "修改时间", index = 14, width = 15)
    private String updateTime;

    private Integer approveStatus;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
