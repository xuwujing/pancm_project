package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "AssetSubsetQueryVO", description = "")
@Data
public class AssetSubsetAddReqVO {
    @NotBlank
    private String subsetName;
    private String sort;
    private String remark;
    @NotBlank
    private String deviceType;
}
