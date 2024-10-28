package com.zans.portal.vo.alert.offlineDevice;

import com.zans.base.util.DateHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 时间分组掉线设备数量
 */
@Data
@ApiModel
public class OfflineDeviceResVO {

    @ApiModelProperty(name = "alertId",value = "警告id")
    private  Integer alertId;

    @ApiModelProperty(name = "keywordValue",value = "关键数据")
    private String keywordValue;

    @ApiModelProperty(name = "deviceType",value = "设备类型")
    private  Integer deviceType;
    @ApiModelProperty(name = "typeName",value = "设备类型name")
    private  String deviceTypeName;

    @ApiModelProperty(name = "deviceModelDes",value = "设备型号")
    private  String deviceModelDes;

    @ApiModelProperty(name = "deviceModelDes",value = "设备品牌")
    private String deviceModelBrand;

    @ApiModelProperty(name = "pointName",value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "ipAddr",value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "mac",value = "mac地址")
    private String mac;

    @ApiModelProperty(name = "alive", value = "1.在线 2离线")
    private Integer alive;

    @ApiModelProperty(name = "aliveLastTime", value = "最后在线时间")
    private Date aliveLastTime;

    @ApiModelProperty(name = "offlineTime",value = "离线时长")
    private String offlineTime;

    @ApiModelProperty(name = "enableStatus",value = "启用状态   0，停用;1,启用")
    private  Integer enableStatus;

    @ApiModelProperty(name = "remark",value = "处置原因")
    private String remark;

    @ApiModelProperty(name = "disposeUser",value = "处置人")
    private String disposeUser;

    @ApiModelProperty(name = "updateId",value = "处置人")
    private  Integer updateId;

    @ApiModelProperty(name = "swAlive",value = "交换机在线状态 1,在线；2,离线")
    private Integer swAlive;

    @ApiModelProperty(name = "offlineType",value = "交换机在线状态 离线原因：0.断光,1掉电")
    private Integer offlineType;

    @ApiModelProperty(name = "groupName",value = "策略组名称")
    private String groupName;

    @ApiModelProperty(name = "noticeTime",value = "告警时间")
    private String noticeTime;

    public Integer getOfflineType() {
        return offlineType==null?0:offlineType;
    }

    public Integer getAlive() {
        if (deviceType != null && deviceType==1){
            if (swAlive != null){
                alive = swAlive;
            }
        }
        return alive;
    }
    public String getAliveName() {
        if (getAlive() !=null && getAlive() == 2){
            if (deviceType != null && deviceType==1){
                if (offlineType == null || offlineType == 0){
                    return "离线（断光）";
                }else {
                    return "离线（掉电）";
                }
            }else {
                return "离线";
            }
        }
        return "在线";
    }

    public String getAliveLastTimeStr() {
        if (aliveLastTime != null){
           return DateHelper.formatDate(aliveLastTime,"yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    public String getOfflineTime() {
        if (aliveLastTime != null){
            return DateHelper.calculateIntervalTime(aliveLastTime);
        }
        return offlineTime;
    }
}
