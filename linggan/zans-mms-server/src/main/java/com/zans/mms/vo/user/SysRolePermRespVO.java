package com.zans.mms.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysRolePerm)实体类
 *
 * @author beixing
 * @since 2021-01-21 14:30:00
 */
@ApiModel(value = "SysRolePermRespVO", description = "")
@Data
public class SysRolePermRespVO  implements Serializable {
    private static final long serialVersionUID = -95813823338686237L;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String roleNum;
    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码")
    private String permNum;
    /**
     * 操作状态
     */
    @ApiModelProperty(value = "操作状态")
    private Integer opStatus;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
