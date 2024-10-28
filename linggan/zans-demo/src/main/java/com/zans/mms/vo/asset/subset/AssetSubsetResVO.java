package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetSubsetQueryVO", description = "")
@Data
public class AssetSubsetResVO {
    /**
     * 资产子集ID
     */
    private Long subsetId;

    /**
     * 子集名称
     */
    private String subsetName;

    /**
     * 排序级别
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String creator;

    private String deviceType;

    private Integer normalCount;
    private Integer stopCount;
    private Integer faultCount;
    private String onlineRate;
    private String lastUpdateTime;


}
