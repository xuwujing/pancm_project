package com.zans.portal.vo.perm;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xv
 * @since 2020/4/13 18:59
 */
@Data
@ApiModel(value="权限编辑对象",description="权限编辑对象")
public class PermissionEditVO {

    @NotNull(message="权限编号必填")
    @JSONField(name = "perm_id")
    @ApiModelProperty(value = "权限编号")
    private Integer permId;

    @NotNull(message="所属模块必填")
    @ApiModelProperty(value = "所属模块")
    private Integer module;

    @NotEmpty(message="权限名称必填")
    @JSONField(name = "perm_name")
    @ApiModelProperty(value = "权限名称", example = "admin", hidden=true)
    private String permName;

    @JSONField(name = "perm_desc")
    @ApiModelProperty(value = "权限描述", example = "admin", hidden=true)
    private String permDesc;

    @ApiModelProperty(value = "前端路由", example = "", hidden=true)
    private String route;

    @ApiModelProperty(value = "后端路由", example = "", hidden=true)
    private String api;

    @ApiModelProperty(value = "显示顺序", example = "", hidden=true)
    private Integer seq;

    private Integer enable;
}
