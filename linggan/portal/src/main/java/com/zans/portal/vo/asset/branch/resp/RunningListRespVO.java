package com.zans.portal.vo.asset.branch.resp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel("运行统计列表返回体")
@Data
public class RunningListRespVO {
    @ApiModelProperty(name = "assetBranchName", value = "设备分组名")
    private String assetBranchName;

    @ApiModelProperty(name = "assetBranchId", value = "设备分组id")
    private Integer assetBranchId;

    @ApiModelProperty(name = "assetBranchStatisticsId", value = "设备分组统计id")
    private Integer assetBranchStatisticsId;

    @ApiModelProperty(name = "branchTotal", value = "设备总数")
    private Integer branchTotal;

    @ApiModelProperty(name = "onlineNumber", value = "设备在线数")
    private Integer onlineNumber;

    @ApiModelProperty(name = "offlineNumber", value = "设备离线数")
    private Integer offlineNumber;

    @ApiModelProperty(name = "disableNumber", value = "设备停用数")
    private Integer disableNumber;

    @ApiModelProperty(name = "onlineRate", value = "在线率")
    private String  onlineRate;

    @ApiModelProperty(name = "creatorId", value = "创建人id")
    private Integer creatorId;

    @ApiModelProperty(name = "createName", value = "创建人")
    private String createName;

    @ApiModelProperty(name = "deleteStatus", value = "0.正常；1.删除")
    private Integer deleteStatus;

    @ApiModelProperty(name = "deleteStatusName", value = "0.正常；1.删除")
    private String deleteStatusName;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(name = "statisticsTime", value = "快照统计时间")
    @JSONField(name = "statisticsTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date statisticsTime;

    public void setDeleteByMap(Map<Object, String> map) {
        Integer status = this.getDeleteStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setDeleteStatusName(name);
    }
}
