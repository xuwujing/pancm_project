package com.zans.mms.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysPermission)实体类
 *
 * @author beixing
 * @since 2021-01-21 14:34:15
 */
@ApiModel(value = "SysPermission", description = "")
@Data
public class SysPermission extends BasePage implements Serializable {
    private static final long serialVersionUID = 484302828981670812L;
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码")
    private String permNum;
    /**
     * 模块
     */
    @ApiModelProperty(value = "模块")
    private String module;
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码")
    private String permName;
    /**
     * 权限描述
     */
    @ApiModelProperty(value = "权限描述")
    private String permDesc;
    /**
     * 前端路径，允许为空
     */
    @ApiModelProperty(value = "前端路径，允许为空")
    private String route;
    /**
     * 接口路径，允许为空
     */
    @ApiModelProperty(value = "接口路径，允许为空")
    private String api;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer seq;
    /**
     * 0，禁用；1，启用
     */
    @ApiModelProperty(value = "0，禁用；1，启用")
    private Integer enable;
    @ApiModelProperty(value = "${column.comment}")
    private Date createTime;
    @ApiModelProperty(value = "${column.comment}")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
