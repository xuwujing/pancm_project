package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class AlertRecordRespVO {

    @ApiModelProperty(name = "id",value = "唯一ID")
    private Long id;


    @ApiModelProperty(name = "alertLevel",value = "告警级别")
    private Integer alertLevel;

    @ApiModelProperty(name = "alertLevelName",value = "告警级别名称")
    private String alertLevelName;

    @ApiModelProperty(name = "strategyName",value = "告警策略名称")
    private String strategyName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private Integer typeId;


    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;

    @ApiModelProperty(name = "indexId",value = "指标ID")
    private String indexId;

    @ApiModelProperty(name = "noticeInfo",value = "告警内容")
    private String noticeInfo;

    @ApiModelProperty(name = "noticeRemark",value = "告警内容备注")
    private String noticeRemark;

    @ApiModelProperty(name = "authMark",value = "意见")
    private String  authMark;

    @ApiModelProperty(name = "noticeStatus",value = "通知状态")
    private Integer noticeStatus;

    @ApiModelProperty(name = "dealStatus",value = "处理状态")
    private Integer dealStatus;

    @ApiModelProperty(name = "dealStatusName",value = "处理状态名称")
    private String dealStatusName;


    @ApiModelProperty(name = "disposeStatus",value = "处置结果")
    private Integer disposeStatus;

    @ApiModelProperty(name = "disposeName",value = "处置结果")
    private String disposeName;

    @ApiModelProperty(name = "alertAction",value = "执行类型")
    private Integer alertAction;

    @ApiModelProperty(name = "action",value = "执行语句")
    private String action;

    @ApiModelProperty(name = "keyword",value = "关键字")
    private String keyword;

    @ApiModelProperty(name = "noticeTime",value = "告警时间")
    private String noticeTime;

    @ApiModelProperty(name = "keywordValue",value = "关键数据")
    private String keywordValue;


    private String noticeMail;

    private String noticeNote;

    private String noticeLocal;

    private String disposeTime;

    private String disposeUser;

    private Integer recordSource;

    private Long ruleId;

    private Long strategyId;

    private String businessId;

    private String createTime;

    private String updateTime;

    private Integer agree;
    /**
     *  以下来源设备信息查询
     */
    @ApiModelProperty(name = "endPointId",value = "设备的ID")
    private Integer endPointId;

    @ApiModelProperty(name = "accessPolicy",value = "状态")
    private Integer accessPolicy;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "brandName", value = "品牌")
    private String brandName;


    @ApiModelProperty(name = "company", value = "厂商")
    private String company;

    @ApiModelProperty(name = "curModelDes", value = "设备型号")
    private String curModelDes;


    @ApiModelProperty(name = "mac", value = "mac")
    private String mac;

    @ApiModelProperty(name = "sysOs", value = "系统")
    private String sysOs;

    @JSONField(name = "aliveLastTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date aliveLastTime;

    @ApiModelProperty(name = "open_port", value = "端口列表")
    private String openPort;

    @ApiModelProperty(name = "mute", value = "是否哑终端(0:否，1:是)")
    private Integer mute;

    @ApiModelProperty(name = "alive", value = "在线状态")
    private Integer alive;


    @ApiModelProperty(name = "user", value = "用户")
    private String user;




    @ApiModelProperty(name = "ipAddr",value = "ip地址")
    private String ipAddr;


    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;



    /**
     * @Author pancm
     * @Description 2020-9-25 新增详情字段显示
     * @Date  2020/9/25
     * @Param
     * @return
     **/
    @ApiModelProperty(name = "acct_start_time", value = "认证时间")
    private String acctStartTime;

    @ApiModelProperty(name = "nas_ip_address", value = "nas地址")
    private String nasIpAddress;

    @ApiModelProperty(name = "nas_name", value = "nas名称")
    private String nasName;

    @ApiModelProperty(name = "nas_type", value = "Nas 类型")
    private String nasType;

    @ApiModelProperty(name = "nas_port_id", value = "Nas 接口")
    private String nasPortId;

    @ApiModelProperty(name = "nas_mac", value = "Nas Mac地址")
    private String nasMac;


    @ApiModelProperty(name = "templateId", value = "模板ID(0:人工处理;1:非法网桥/网段处理;2:ip冲突)")
    private Integer templateId;

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



    @ApiModelProperty(name = "deleteStatus", value = "删除状态 0:否,1:是")
    private Integer deleteStatus;

    @ApiModelProperty(name = "sqlJson", value = "规则查询sql的json数据")
    private String sqlJson;

    @ApiModelProperty(name = "isRead", value = "是否已读  0否 1是")
    private String isRead;

    @ApiModelProperty(name = "groupName", value = "策略组名称")
    private String groupName;

    @ApiModelProperty(name = "recoverStatus", value = "恢复状态")
    private String recoverStatus;

    @ApiModelProperty(name = "recoverTime", value = "恢复时间")
    private String recoverTime;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
