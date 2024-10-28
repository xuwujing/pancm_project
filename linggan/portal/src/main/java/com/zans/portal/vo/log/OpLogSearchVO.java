package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class OpLogSearchVO extends BasePage {

    @JSONField(name = "user_name")
    @ApiModelProperty(name = "user_name",value = "用户名")
    private String userName;

    @ApiModelProperty(name = "role",value = "用户角色")
    private Integer role;

    @ApiModelProperty(name = "module",value = "操作模块")
    private Integer module;

    @ApiModelProperty(name = "content",value = "操作内容")
    private String content;

}
