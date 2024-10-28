package com.zans.mms.vo.asset.subset;

import com.zans.mms.vo.asset.AssetQueryVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class AssetChooseReqVO extends AssetQueryVO {
    @NotNull(message = "subsetId not null")
    private Long subsetId;
    @NotBlank(message = "deviceType not null")
    private String deviceType;
}
