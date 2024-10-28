package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "PatrolTaskResVO", description = "")
@Data
public class PatrolTaskResVO {
    private String  taskName;
    private String  description;
    private String  startDate;
    private String  endDate;
    private Integer pointCount;
    private Integer finishedCount;
    private String  executeStatus;
    private BigDecimal  finishedRate;
    private String  enableStatus;
    private Long    id;

    private String orgId;
    private String createTime;

    private Integer surplusDays;


}
