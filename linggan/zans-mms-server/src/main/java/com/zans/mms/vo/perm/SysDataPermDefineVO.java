package com.zans.mms.vo.perm;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDataPermDefineVO implements Serializable {
    private Long id;

    /**
     * sys_permission.perm_id
     */
    @JSONField(name = "perm_id")
    private Integer permId;

    /**
     * 数据权限名称
     */
    @JSONField(name = "data_name")
    private String dataName;

    /**
     * 数据权限数值
     */
    @JSONField(name = "data_value")
    private Integer dataValue;

    /**
     * 描述
     */
    private String remark;

    /**
     * 权限分组
     */
    @JSONField(name = "data_group")
    private Integer dataGroup;


    private static final long serialVersionUID = 1L;


}