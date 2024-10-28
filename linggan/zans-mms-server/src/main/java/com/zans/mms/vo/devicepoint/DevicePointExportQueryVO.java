package com.zans.mms.vo.devicepoint;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "DevicePointExportQueryVO", description = "")
@Data
public class DevicePointExportQueryVO  implements Serializable {
    private String pointName;

    private String pointCode;

    private List<String> areaId;

    private Long  subsetId;

    private List<String>  deviceType;

    /**
     * 所属单位id
     */
    private List<String> orgId;

    @ApiModelProperty(name = "pointIds", value = "已存在的编号集合",hidden = true)
    private List<Long> pointIds;
}
