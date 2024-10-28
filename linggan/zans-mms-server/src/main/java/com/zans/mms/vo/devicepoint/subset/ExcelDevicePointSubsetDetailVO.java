package com.zans.mms.vo.devicepoint.subset;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel("巡检子集信息导入excle模板实体")
@Data
@ToString
public class ExcelDevicePointSubsetDetailVO {

    @ApiModelProperty(value = "子集名称")
    @ExcelProperty(value = "子集名称", index = 0, validate = {"not_empty"})
    private String subsetName;

    @ApiModelProperty(value = "点位编号")
    @ExcelProperty(value = "点位编号", index = 1, validate = {"not_empty"})
    private String pointCode;

    @ApiModelProperty(value = "点位名称")
    @ExcelProperty(value = "点位名称", index = 2)
    private String pointName;




    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;





}
