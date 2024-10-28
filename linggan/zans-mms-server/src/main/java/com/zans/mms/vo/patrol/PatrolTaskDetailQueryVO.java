package com.zans.mms.vo.patrol;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "PatrolTaskQueryVO", description = "")
@Data
public class PatrolTaskDetailQueryVO extends PatrolTaskQueryVO {

    private Integer checkStatus;

}
