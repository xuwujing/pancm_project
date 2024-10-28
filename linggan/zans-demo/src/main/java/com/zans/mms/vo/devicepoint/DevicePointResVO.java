package com.zans.mms.vo.devicepoint;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "DevicePointQueryVo", description = "")
@Data
public class DevicePointResVO {
    private String areaId;

    private String pointName;

    private String pointCode;

    private String deviceType;

    private String longitude;

    private String latitude;

    private Long pointId;

    private String checkStatus;

}
