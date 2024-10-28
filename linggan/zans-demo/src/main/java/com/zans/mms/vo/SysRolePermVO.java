package com.zans.mms.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (SysRolePerm)实体类
 *
 * @author beixing
 * @since 2021-01-21 14:34:05
 */
@ApiModel(value = "SysRolePerm", description = "")
@Data
public class SysRolePermVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -46930144757682524L;
    private Integer id;

    private String roleId;

    private Integer permId;

    //TODO List<Integer>
    @ApiModelProperty(value = "1,2,3多个逗号分割",name = "1,2,3多个逗号分割")
    private String dataPermDesc;

    private String createTime;





    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
