package com.zans.mms.vo.user;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserSearchReqVO extends BasePage {


    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户代码")
    private String userName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "用户名称")
    private String nickName;

    /**
     * 所属角色
     */
    @ApiModelProperty(value = "所属角色")
    private String roleNum;
    /**
     * 所属单位
     */
    @ApiModelProperty(value = "所属单位")
    private String maintainNum;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;
}
