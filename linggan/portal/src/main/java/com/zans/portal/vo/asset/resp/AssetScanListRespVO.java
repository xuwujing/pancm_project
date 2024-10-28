package com.zans.portal.vo.asset.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class AssetScanListRespVO extends BasePage {

    @ApiModelProperty(value = "id")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(value = "在线状态")
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(value = "离线原因：0.断网,1断电")
    @JSONField(name = "offlineType")
    private Integer offlineType;

    @ApiModelProperty(value = "开始时间")
    @JSONField(name = "begin_time")
    private String beginTime;

    @ApiModelProperty(value = "截至时间")
    @JSONField(name = "end_time")
    private String endTime;

    @ApiModelProperty(value = "持续时长")
    @JSONField(name = "duration_seconds")
    private String durationSeconds;

    @ApiModelProperty(value = "监控时间")
    @JSONField(name = "scan_time")
    private String scanTime;



}
