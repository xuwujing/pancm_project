package com.zans.mms.vo.asset.subset;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ApiModel("资产子集导入excel模板实体")
@Data
@ToString
public class ExcelAssetSubsetVO {

    @ApiModelProperty(value = "子集名称")
    @ExcelProperty(value = "子集名称", index = 0, validate = {"not_empty"})
    private String subsetName;


    @ApiModelProperty(value = "设备类型")
    @ExcelProperty(value = "设备类型", index = 1, validate = {"not_empty"})
    private String deviceType;


    @ApiModelProperty(value = "设备编号")
    @ExcelProperty(value = "设备编号", index = 2, validate = {"not_empty"})
    private String assetCode;

    @ApiModelProperty(value = "点位名称")
    @ExcelProperty(value = "点位名称", index = 3)
    private String pointName;


    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    private Long assetId;

    private String subsetId;

    private Date createTime;

    private Date updateTime;



}
