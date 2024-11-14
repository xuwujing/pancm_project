package com.zans.mms.vo.role;


import com.zans.mms.vo.SysRolePermVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value="Role修改对象",description="Role修改对象")
public class RoleEditVO {

    @NotEmpty(message="角色ID必填")
    private String roleId;

    @NotEmpty(message="角色名必填")
    @ApiModelProperty(value = "用户姓名", example = "")
    private String roleName;

    @ApiModelProperty(value = "角色描述", example = "")
    private String remark;

    @ApiModelProperty(value = "提交的权限列表")
    private List<Integer> checkedPermission;

    @ApiModelProperty(value = "checkedDataPerm")
    private List<SysRolePermVO> checkedDataPerm;
    /**
     *     public static Integer MODULE_TYPE_PC = 1;
    public static Integer MODULE_TYPE_APP = 2;
     */
    @ApiModelProperty(value = "moduleType: PC = 1   APP = 2")
    @NotNull
    private Integer moduleType;

}