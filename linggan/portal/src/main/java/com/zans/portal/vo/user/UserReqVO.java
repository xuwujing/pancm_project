package com.zans.portal.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class UserReqVO extends BasePage {

    @JSONField(name = "user_name")
    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "角色")
    private Integer role;

    @JSONField(name = "nick_name")
    private String nickName;

    @JSONField(name = "department")
    private Integer department;

    @JSONField(name = "mobile")
    private String mobile;

    private List<Integer> departmentIds;

}
