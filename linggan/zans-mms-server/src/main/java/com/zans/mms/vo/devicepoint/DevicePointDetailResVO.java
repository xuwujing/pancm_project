package com.zans.mms.vo.devicepoint;

import com.zans.mms.vo.asset.AssetResVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "DevicePointDetailResVo", description = "")
@Data
public class DevicePointDetailResVO {
    private String areaId;
    private String pointName;
    private String pointCode;
    private String powerWay;
    private String powerPlace ;
    private String projectId;
    private String deviceType;
    private String longitude;
    private String latitude;
    private String roadType;
    private List<AssetResVO> assetList;
}
