package com.zans.portal.vo.network;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xv
 * @since 2020/6/4 20:07
 */
@ApiModel
@Data
public class PointVO {

    @ApiModelProperty(value = "点位ID")
    private Integer id;

    @ApiModelProperty(value = "链路ID")
    @JSONField(name = "link_id")
    private Integer linkId;

    @ApiModelProperty(value = "项目ID")
    @JSONField(name = "project_id")
    private Integer projectId;

    @ApiModelProperty(value = "点位名称")
    @JSONField(name = "point_name")
    @ExcelProperty(value = {"下挂点位名称"}, validate = {"not_empty"}, index = 8)
    private String pointName;

    @ApiModelProperty(value = "点位编号")
    @JSONField(name = "point_code")
    @ExcelProperty(value = {"点位编号"}, validate = {"not_empty"}, index = 10)
    private String pointCode;

    @ApiModelProperty(value = "设备类型编号")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "设备类型名称")
    @JSONField(name = "device_type_name")
    @ExcelProperty(value = {"设备类型"}, validate = {"not_empty"}, index = 9)
    private String deviceTypeName;

    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;
}
