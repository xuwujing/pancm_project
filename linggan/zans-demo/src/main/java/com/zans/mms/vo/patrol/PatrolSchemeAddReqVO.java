package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "PatrolSchemeAddReqVO", description = "")
@Data
public class PatrolSchemeAddReqVO {
    private String schemeName;
    private String description;
    private Integer patrolPeriod;
    private String orgId;
    private Long   subsetId;
    private String enableStatus;

}
