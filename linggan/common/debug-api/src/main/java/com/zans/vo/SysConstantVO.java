package com.zans.vo;

import java.util.Date;
import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * @author beixing
 * @Title: 常量表(SysConstant)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-03-15 16:36:43
 */
@Data
public class SysConstantVO implements Serializable {
    private static final long serialVersionUID = -65947230656167627L;
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
