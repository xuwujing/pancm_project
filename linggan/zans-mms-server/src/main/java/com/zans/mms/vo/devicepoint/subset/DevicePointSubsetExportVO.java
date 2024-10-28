package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "DevicePointSubsetExportVO", description = "巡检子集导出实体")
@Data
public class DevicePointSubsetExportVO {

    /**
     * 子集名称
     */
    private String subsetName;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 辖区
     */
    private String areaName;

    /**
     * 点位类型
     */
    private String deviceType;

    /**
     * 点位编号
     */
    private String pointCode;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;
}
