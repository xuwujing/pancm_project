package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "AssetSubsetEditReqVO", description = "")
@Data
public class AssetSubsetEditReqVO {
    private String subsetName;
    private String sort;
    private String remark;
    @NotBlank
    private Long subsetId;
}
