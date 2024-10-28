package com.zans.mms.vo.patrol;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* @Title: PatrolTaskQueryVO
* @Description: 巡检任务请求体
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/20/21
*/
@ApiModel(value = "PatrolTaskQueryVO", description = "")
@Data
public class PatrolTaskQueryVO extends BasePage implements Serializable {

    private String taskName;

    private Long patrolTaskId;

    private String patrolDate;

    @ApiModelProperty(value = "数据权限", name = "数据权限",hidden = true)
    private Integer dataPerm;

    @ApiModelProperty(value = "数据权限", name = "userName",hidden = true)
    private String userNamePerm;
    @ApiModelProperty(value = "数据权限", name = "orgId",hidden = true)
    private String orgIdPerm;

    @ApiModelProperty(value = "运维单位", name = "orgIds")
    private List<String> orgIds;
}
