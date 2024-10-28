package com.zans.vo;

import com.zans.vo.excel.ExcelProperty;
import lombok.Data;

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
public class DeviceCipherExcelImport  implements Serializable {

    @ExcelProperty(value = "IP地址", index = 0, width = 15)
    private String ip;
    /**
     * 大华、海康、宇视等
     */
    @ExcelProperty(value = "设备品牌", index = 1, width = 10)
    private String deviceBrand;
    /**
     * 不可修改
     */
    @ExcelProperty(value = "账号", index = 2, width = 15)
    private String account;
    /**
     * AES加密
     */
    @ExcelProperty(value = "密码", index = 3, width = 15)
    private String password;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 4, width = 15)
    private String remark;

}
