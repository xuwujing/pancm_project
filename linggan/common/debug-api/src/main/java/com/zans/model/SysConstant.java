package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 常量表(SysConstant)实体类
 *
 * @author beixing
 * @since 2022-03-15 16:37:52
 */
@Data
@Table(name = "sys_constant")
public class SysConstant implements Serializable {
    private static final long serialVersionUID = -69710691261947100L;
    @Column(name = "id")
    private Integer id;
    /**
     * 键
     */
    @Column(name = "constant_key")
    private String constantKey;
    /**
     * 常量的值
     */
    @Column(name = "constant_value")
    private String constantValue;
    /**
     * 是否启用，默认1启用0不启用
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 备注（显示名称）
     */
    @Column(name = "comment")
    private String comment;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
