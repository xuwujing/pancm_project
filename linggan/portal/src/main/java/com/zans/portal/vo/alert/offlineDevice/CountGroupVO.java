package com.zans.portal.vo.alert.offlineDevice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 时间分组掉线设备数量
 */
@Data
@Builder
@ApiModel
public class CountGroupVO {
    public static final int GROUP0 = 0;
    public static final int GROUP3 = 3;
    public static final int GROUP7 = 7;
    public static final int GROUP14 = 14;
    public static final int GROUP30 = 30;
    public static final int GROUP31 = 31;

    public static final String GROUP_NAME = "全部";
    public static final String GROUP_NAME3 = "近三天离线";
    public static final String GROUP_NAME7 = "近七天离线";
    public static final String GROUP_NAME14 = "近两周离线";
    public static final String GROUP_NAME30 = "近一个月离线";
    public static final String GROUP_NAME31 = "一个月以上";

    @ApiModelProperty(name = "group",value = "分组id,0全部、3近三天、7近7天、14近两周、30近一个月、31一个月外")
    private Integer group;

    @ApiModelProperty(name = "groupName",value = "分组名称；全部、近三天离线")
    private String groupName;

    @ApiModelProperty(name = "count",value = "统计数量")
    private Integer count;
}
