package com.zans.portal.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


@ApiModel
@Data
@Validated
public class QzJudgeReqVO {

    @ApiModelProperty(name = "id", value = "id", required = true)
    @JSONField(name = "id")
    @NotNull
    private Integer id;

    @ApiModelProperty(name = "auth_mark", value = "审核意见", required = true)
    @JSONField(name = "auth_mark")
    @NotNull
    private String authMark;

    @ApiModelProperty(name = "judge_type", value = "处理类型: 0阻断、1检查、2放行", required = true)
    @JSONField(name = "judge_type")
    @NotNull
    private Integer judgeType;

}
