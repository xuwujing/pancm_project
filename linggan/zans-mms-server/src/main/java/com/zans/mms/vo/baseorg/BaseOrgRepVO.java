package com.zans.mms.vo.baseorg;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 单位维护 base_org表(BaseOrg)实体类
 *
 * @author beixing
 * @since 2021-03-24 16:01:37
 */
@ApiModel(value = "BaseOrgRepVO", description = "单位维护表")
@Data
public class BaseOrgRepVO  implements Serializable {
    private static final long serialVersionUID = -13471831755836740L;
    /**
     * 机构代码
     */
    @ApiModelProperty(value = "机构代码")
    private String orgId;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String orgName;
    /**
     * 单位全称
     */
    @ApiModelProperty(value = "单位全称")
    private String orgFullName;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String orgContact;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String orgPhone;
    /**
     * 单位类型
     */
    @ApiModelProperty(value = "单位类型")
    private String orgType;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建用户
     */
    @ApiModelProperty(value = "创建用户")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
