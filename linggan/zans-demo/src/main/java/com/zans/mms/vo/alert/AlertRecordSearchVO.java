package com.zans.mms.vo.alert;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class AlertRecordSearchVO extends BasePage {

    @ApiModelProperty(name = "id",value = "唯一ID")
    private Long id;

    @ApiModelProperty(name = "alertLevel",value = "告警级别")
    private Integer alertLevel;

    @ApiModelProperty(name = "strategyName",value = "告警策略名称")
    private String strategyName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private  Integer typeId;

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "noticeRemark",value = "备注")
    private String noticeRemark;


    @ApiModelProperty(name = "noticeStatus",value = "通知状态")
    private Integer noticeStatus;

    @ApiModelProperty(name = "dealStatus",value = "处理状态")
    private Integer dealStatus;

    @ApiModelProperty(name = "disposeStatus",value = "处置结果 0:阻断,2:放行,3:基线更新,4:阻断mac地址")
    private Integer disposeStatus;

    @ApiModelProperty(name = "alertAction",value = "执行类型")
    private Integer alertAction;

    @ApiModelProperty(name = "action",value = "执行语句")
    private String action;

    @ApiModelProperty(name = "keyword",value = "关键字")
    private String keyword;

    @ApiModelProperty(name = "keywordValue",value = "关键字内容")
    private String keywordValue;

    @ApiModelProperty(name = "noticeInfo",value = "告警内容")
    private String noticeInfo;

    @ApiModelProperty(name = "indexId",value = "指标ID")
    private Integer indexId;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;

    @ApiModelProperty(name = "startTime",value = "查询开始时间")
    private String startTime;

    @ApiModelProperty(name = "endTime",value = "查询结束时间")
    private String endTime;

    @ApiModelProperty(name = "agree",value = "同意状态,0:不同意,1:同意")
    private Integer agree;

    @ApiModelProperty(name = "templateId", value = "模板ID(0:人工处理;1:非法网桥/网段处理;2:ip冲突)")
    private Integer templateId;

    /**
     * @Author pancm
     * @Description 新增查询字段
     * @Date  2020/9/29
     * @Param
     * @return
     **/
    @ApiModelProperty(name = "username",value = "mac地址")
    private String username;

    @ApiModelProperty(name = "ipAddr",value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "areaId", value = "区域")
    private Integer areaId;

    @ApiModelProperty(name = "areaName", value = "所属区域")
    private String areaName;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "user",value = "用户")
    private String user;

    @ApiModelProperty(name = "endPointId",value = "设备的ID")
    private Integer endPointId;

    @ApiModelProperty(name = "accessPolicy",value = "审核状态")
    private Integer accessPolicy;

    @ApiModelProperty(name = "authMark",value = "意见")
    private String  authMark;

    /**
     * @Author pancm
     * @Description 仅供 点位变更规则使用
     * @Date  2020/10/8
     * @Param
     * @return
     **/
    @ApiModelProperty(name = "macs", value = "阻断设备Mac地址，前面的是基准mac，后面的是当前的mac")
    private String[] macs;

    @ApiModelProperty(name = "chooseMacType", value = "选择的mac,0:基准,1:当前")
    private Integer chooseMacType;

    @ApiModelProperty(name = "isRead", value = "已读状态")
    private Integer isRead;

    @ApiModelProperty(name = "noticeTime", value = "告警时间")
    private Integer noticeTime;

    private List<AlertReportRespVO> macList;

}
