package com.zans.mms.vo.asset.subset;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@ApiModel(value = "AssetSubsetDetaiQueryReqVO", description = "")
@Data
public class AssetSubsetDetaiQueryReqVO extends BasePage implements Serializable {

    @NotNull
    private Long subsetId;

    private String pointName;

    private String pointCode;
    private String areaId;
    private String assetCode;
    private String maintainStatus;
    private String networkIp;
    private String projectName;
    private String deviceType;
    private String onlineStatus;

    /**
     * 所属单位id
     */
    private String orgId;
}
