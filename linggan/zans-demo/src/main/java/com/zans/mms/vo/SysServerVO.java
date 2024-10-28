package com.zans.mms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "SysServerVO", description = "")
@Data
public class SysServerVO {
    private String projectName;

    private String serverIp;
    /**
     * 0.stop   1.start
     */
    private Integer stopAndStartFlag;

}
