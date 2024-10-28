package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class SwitchBranchAcceptVO {

    @ApiModelProperty(name = "ids", value = "ids")
    @NotNull(message = "ids")
    private List<Integer> ids;


    @ApiModelProperty(name = "acceptance", value = "状态 0未验收 1 已验收")
    @NotNull(message = "状态")
    private Integer acceptance;

    @ApiModelProperty(name = "acceptDate", value = "验收时间格式 yyyy-MM-dd")
    @NotNull(message = "验收时间")
    private Date acceptDate;

    @ApiModelProperty(name = "acceptIdea", value = "验收意见")
    private String acceptIdea;


}
