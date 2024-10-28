package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "PatrolTaskDetailResVO", description = "")
@Data
public class PatrolTaskDetailResVO {
    private String taskName;
    private String orgId;
    private String createTime;
    private String startDate;
    private String endDate;
    private Integer pointCount;
    private Integer finishedCount;
    private String execute_status;
    private BigDecimal finishedRate;
    private String enableStatus;
    private Long id;

    private List<TaskDetailVO> taskList;

}
