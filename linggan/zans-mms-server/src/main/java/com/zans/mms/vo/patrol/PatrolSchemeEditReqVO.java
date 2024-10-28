package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value = "PatrolSchemeEditReqVO", description = "")
@Data
public class PatrolSchemeEditReqVO {
    private String schemeName;
    private String description;
    private Integer patrolPeriod;
    private String orgId;
    private Long   subsetId;
    private String enableStatus;
    @NotNull
    private Long   id;

}
