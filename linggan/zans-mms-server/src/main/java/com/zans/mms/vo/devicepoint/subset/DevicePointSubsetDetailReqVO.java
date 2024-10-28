package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("巡检子集导入接收实体")
public class DevicePointSubsetDetailReqVO {
    /**
     * 是否清空原有数据
     */
    private Boolean isClean;

    /**
     * 是否新建不存在子集
     */
    private Boolean isCreate;
}
