package com.zans.mms.vo.asset.subset;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("资产子集导入接收实体")
public class AssetSubsetDetailReqVO {

    /**
     * 是否清空原有数据
     */
    private Boolean isClean;

    /**
     * 是否新建不存在子集
     */
    private Boolean isCreate;
}
