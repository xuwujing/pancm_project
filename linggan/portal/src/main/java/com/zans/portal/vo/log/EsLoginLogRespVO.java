package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/6
 */
@ApiModel
@Data
public class EsLoginLogRespVO extends BasePage implements Serializable {

    @ApiModelProperty(name = "userName",value = "登录账号")
    private String userName;

    @ApiModelProperty(name = "roleName",value = "登录角色")
    private String roleName;

    @ApiModelProperty(name = "loginTimeStart",value = "登录起始时间")
    private String loginTimeStart;

    @ApiModelProperty(name = "loginTimeEnd",value = "登录结束时间")
    private String loginTimeEnd;



}
