package com.zans.mms.vo.devicepoint;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ApiModel("点位信息导入excle模板实体")
@Data
@ToString
public class ExcelDevicePointVO {

    @ApiModelProperty(value = "点位编号")
    @ExcelProperty(value = "点位编号", index = 0, validate = {"not_empty"})
    private String pointCode;

    @ApiModelProperty(value = "修改之后点位编号")
    @ExcelProperty(value = "修改之后点位编号", index = 1)
    private String pointCodeChange;

    @ApiModelProperty(value = "点位名称")
    @ExcelProperty(value = "点位名称", index = 2, validate = {"not_empty"})
    private String pointName;

    @ApiModelProperty(value = "辖区名称")
    @ExcelProperty(value = "辖区名称", index = 3, validate = {"not_empty"})
    private String areaName;

    @ApiModelProperty(value = "路口类型")
    @ExcelProperty(value = "路口类型", index = 4)
    private String roadType;

    @ApiModelProperty(value = "设备类型")
    @ExcelProperty(value = "设备类型", index = 5, validate = {"not_empty"})
    private String deviceType;


    @ApiModelProperty(value = "取电方式/位置")
    @ExcelProperty(value = "取电方式/位置", index = 6)
    private String powerWay;

    @ApiModelProperty(value = "通讯接入方式")
    @ExcelProperty(value = "通讯接入方式", index = 7)
    private String networkLinkway;

    @ApiModelProperty(value = "经度")
    @ExcelProperty(value = "经度", index = 8, validate = {"not_empty"})
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @ExcelProperty(value = "纬度", index = 9, validate = {"not_empty"})
    private String latitude;

    @ApiModelProperty(value = "地图来源")
    private String mapSource;


    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    private Integer wechatEnable;

    //辖区id
    private String areaId;

    private String creator;

    private Date createTime;

    private Date updateTime;

    private String gis;

    private Integer pointType;

    private Integer isTest;

}
