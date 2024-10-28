package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "PatrolSchemeQueryVO", description = "")
@Data
public class PatrolSchemeResVO {
    private String schemeName;
    private String description;
    private Integer patrolPeriod;
    private Integer pointCount;
    private String orgId;
    private Long   subsetId;
    private String creator;
    private String updateTime;
    private String createTime;
    private String enableStatus;
    private Long   id;
    private String preTime;

}
