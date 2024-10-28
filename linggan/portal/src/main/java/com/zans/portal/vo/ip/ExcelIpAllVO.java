package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import com.zans.base.vo.SelectVO;
import com.zans.portal.vo.chart.OnlineCircleUnit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class ExcelIpAllVO {

    @ApiModelProperty(value = "序号")
    @ExcelProperty(value = "序号", type="Integer", index = 0)
    private Integer seq;

    @ApiModelProperty(value = "IP地址")
    @ExcelProperty(value = "IP地址", index = 1, validate = {"not_empty"})
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(value = "设备编号")
    @ExcelProperty(value = "设备编号", index = 2, validate = {"not_empty"})
    @JSONField(name = "device_model_des")
    private String deviceModelDes;

    @ApiModelProperty(value = "点位名称")
    @ExcelProperty(value = "点位名称", index = 3, validate = {"not_empty"})
    @JSONField(name = "point_name")
    private String pointName;

    @ApiModelProperty(value = "所在辖区")
    @ExcelProperty(value = "所在辖区", index = 4, validate = {"not_empty"})
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(value = "单位名称")
    @ExcelProperty(value = "单位名称", index = 5, validate = {"not_empty"})
    @JSONField(name = "department_name")
    private String departmentName;

    @ApiModelProperty(value = "经度")
    @ExcelProperty(value = "经度", index = 6, validate = {"not_empty"})
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @ExcelProperty(value = "纬度", index = 7, validate = {"not_empty"})
    private String latitude;

    @ApiModelProperty(value = "项目名称")
    @ExcelProperty(value = "项目名称", index = 8, validate = {"not_empty"})
    @JSONField(name = "project_name")
    private String projectName;


    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;



    private Integer departmentId;


    public void resetDepartment(List<OnlineCircleUnit> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (OnlineCircleUnit unit : list) {
            if (departmentName.equals(unit.getName())) {
                this.departmentId = unit.getId();
                break;
            }
        }
    }
}
