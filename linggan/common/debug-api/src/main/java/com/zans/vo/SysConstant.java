package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 常量表(SysConstant)实体类
 *
 * @author beixing
 * @since 2022-03-15 16:37:51
 */
@Data
public class SysConstant implements Serializable {
    private static final long serialVersionUID = -28239535766882361L;
    private Integer id;
    /**
     * 键
     */
    private String constantKey;
    /**
     * 常量的值
     */
    private String constantValue;
    /**
     * 是否启用，默认1启用0不启用
     */
    private Integer status;
    /**
     * 备注（显示名称）
     */
    private String comment;
    private Date createTime;
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
