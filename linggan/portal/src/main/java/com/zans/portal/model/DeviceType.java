package com.zans.portal.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Table(name = "device_type_guard")
public class DeviceType implements Serializable {
    /**
     * 设备类别编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    /**
     * 设备类别
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 是否哑终端 0：否 1：是
     */
    @Column(name = "mute")
    private Integer mute;

    /**
     * 模板地址，相对路径
     */
    private String template;

    /**
     * 是否启用 0：否 1：是
     */
    @Column(name = "enable_status")
    private Integer enableStatus;


    private static final long serialVersionUID = 1L;


}
