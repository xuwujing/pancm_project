package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsOperationLogRespVO extends BasePage {

    @ApiModelProperty(name = "operationName",value = "操作人")
    private String operationName;

    @ApiModelProperty(name = "operationUser",value = "操作角色")
    private String operationUser;

    @ApiModelProperty(name = "operationTimeStart",value = "操作起始时间")
    private String operationTimeStart;

    @ApiModelProperty(name = "operationTimeEnd",value = "登录结束时间")
    private String operationTimeEnd;

    @ApiModelProperty(name = "operationType",value = "操作类型")
    private String operationType;

    @ApiModelProperty(name = "module",value = "所属模块")
    private String module;

}
