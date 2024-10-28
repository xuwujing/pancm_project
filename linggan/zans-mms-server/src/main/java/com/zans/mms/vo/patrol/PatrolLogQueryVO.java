package com.zans.mms.vo.patrol;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PatrolLogQueryVO", description = "")
@Data
public class PatrolLogQueryVO extends BasePage implements Serializable {


    @ApiModelProperty(value = "辖区", name = "辖区")
    private String areaId;

    @ApiModelProperty(value = "设备类型", name = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "点位名称", name = "点位名称")
    private String pointName;

    @ApiModelProperty(value = "点位编号", name = "点位编号")
    private String pointCode;

    @ApiModelProperty(value = "巡检前状态", name = "巡检前状态")
    private String prevCheckResult;

    @ApiModelProperty(value = "巡检后状态", name = "巡检后状态")
    private String checkResult;

    @ApiModelProperty(value = "巡检打卡人名", name = "巡检打卡人名")
    private String nickName;

    @ApiModelProperty(value = "数据权限", name = "数据权限",hidden = true)
    private Integer dataPerm;

    @ApiModelProperty(value = "数据权限", name = "userName",hidden = true)
    private String userNamePerm;
    @ApiModelProperty(value = "数据权限", name = "orgIdPerm",hidden = true)
    private String orgIdPerm;

    @ApiModelProperty(value = "单位ids", name = "orgIds")
    private List<String> orgIds;

    @ApiModelProperty(value = "单位ids", name = "tempOrgIds",hidden = true)
    private List<String> tempOrgIds;

    private Long id;
}
