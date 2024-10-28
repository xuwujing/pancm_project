package com.zans.mms.vo.asset.subset;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AssetSubsetQueryVO", description = "")
@Data
public class AssetSubsetQueryVO extends BasePage implements Serializable {
    /**
     * 资产子集ID
     */
    private Long subsetId;

    /**
     * 子集名称
     */
    private String subsetName;

    private List<String> deviceType;

    private String remark;
}
