package com.zans.portal.vo.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.model.TUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value="Role新增对象",description="Role新增对象")
public class RoleAddVO {

    @NotEmpty(message="角色名必填")
    @JSONField(name = "role_name")
    @ApiModelProperty(value = "用户姓名", example = "admin", hidden=true)
    private String roleName;

    @JSONField(name = "role_desc")
    @ApiModelProperty(value = "角色描述", example = "admin", hidden=true)
    private String roleDesc;

    @ApiModelProperty(value = "角色状态，启用禁用", example = "0 | 1", hidden=true)
    private Integer enable;

    @JSONField(name = "perm_ids")
    @ApiModelProperty(value = "提交的权限列表", example = "[1,2,3]", hidden=true)
    private List<Integer> permIds;

}
