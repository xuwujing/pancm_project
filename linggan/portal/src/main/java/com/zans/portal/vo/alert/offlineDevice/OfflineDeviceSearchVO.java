package com.zans.portal.vo.alert.offlineDevice;

import com.zans.base.util.DateHelper;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 时间分组掉线设备数量
 */
@Data
@ApiModel
public class OfflineDeviceSearchVO extends BasePage {

    @ApiModelProperty(name = "group",value = "分组id,0全部、3近三天、7近7天、14近两周、30近一个月、31一个月外")
    private Integer group;

    @ApiModelProperty(name = "ipAddr",value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "mac",value = "mac地址")
    private String mac;

    @ApiModelProperty(name = "pointName",value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "deviceType",value = "设备类型")
    private  Integer deviceType;

    @ApiModelProperty(name = "enableStatus",value = "启用状态")
    private  Integer enableStatus;

    @ApiModelProperty(name = "isRead",value = "已读状态")
    private  Integer isRead;


    public Date getAliveLastTime(){
        if (group == null){
            return  null;
        }
        Date currDate = new Date();
        if (group == 0){
            return null;
        }else if (group > 0 && group < 31){
           return DateHelper.plusDays(currDate,-group);
        }
        return null;
    }
    //一个月以前
    public Date getBeforeAliveLastTime(){
        if (group == null){
            return  null;
        }
        Date currDate = new Date();
        if ( group == 31){
            return DateHelper.plusDays(currDate,-30);
        }
        return null;
    }

}
