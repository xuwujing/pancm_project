package com.zans.portal.vo.asset.req;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import com.zans.base.vo.SelectVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("资产导入excle模板实体")
@Data
public class ExcelEndpointIpAllVO {

    @ApiModelProperty(value = "IP地址")
    @ExcelProperty(value = "IP地址", index = 0, validate = {"not_empty"})
    private String ipAddr;


    @ApiModelProperty(value = "点位名称")
    @ExcelProperty(value = "点位名称", index = 1, validate = {"not_empty"})
    private String pointName;


//    //需要转换
//    @ApiModelProperty(value = "所属辖区")
//    @ExcelProperty(value = "所属辖区", index = 2, validate = {"not_empty"})
//    private String areaName;

    //需要转换
    @ApiModelProperty(value = "设备类型")
    @ExcelProperty(value = "设备类型", index = 2,validate = {"not_empty"})
    private String deviceTypeName;

    //需要转换
    @ApiModelProperty(value = "维护状态")
    @ExcelProperty(value = "维护状态", index = 3)
    private String maintainStatusName;

    //需要转换
    @ApiModelProperty(value = "是否哑终端")
    @ExcelProperty(value = "是否哑终端", index = 4,validate = {"not_empty"})
    private String muteName;


    @ApiModelProperty(value = "项目名称")
    @ExcelProperty(value = "项目名称", index = 5)
    private String projectName;


    
    @ApiModelProperty(value = "设备品牌")
    @ExcelProperty(value = "设备品牌", index = 6)
    private String deviceModelBrand;

    
    @ApiModelProperty(value = "设备型号")
    @ExcelProperty(value = "设备型号", index = 7)
    private String deviceModelDes;

    @ApiModelProperty(value = "承建单位")
    @ExcelProperty(value = "承建单位", index = 8)
    private String contractor;

    @ApiModelProperty(value = "承建联系人")
    @ExcelProperty(value = "承建联系人", index = 9)
    private String contractorPerson;

    @ApiModelProperty(value = "承建电话")
    @ExcelProperty(value = "承建电话", index = 10)
    private String contractorPhone;

    @ApiModelProperty(value = "维护单位")
    @ExcelProperty(value = "维护单位", index = 11)
    private String maintainCompany;

    @ApiModelProperty(value = "维护联系人")
    @ExcelProperty(value = "维护联系人", index = 12)
    private String maintainPerson;

    @ApiModelProperty(value = "维护电话")
    @ExcelProperty(value = "维护电话", index = 13)
    private String maintainPhone;

    @ApiModelProperty(value = "经度")
    @ExcelProperty(value = "经度", index = 14)
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @ExcelProperty(value = "纬度", index = 15)
    private String latitude;

    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    private Integer deviceType;

    private Integer maintainStatus;

    private Integer enableStatus;



    public void resetDeviceType(List<SelectVO> list) {
        if (list == null) {
            return;
        }
        for (SelectVO vo : list) {
            vo.resetKey();
            if (deviceTypeName.equals(vo.getItemValue())) {
                this.deviceType = (Integer) vo.getItemKey();
                break;
            }
        }
    }

    public void resetMaintainStatus(List<SelectVO> list) {
        if (list == null) {
            return;
        }
        for (SelectVO vo : list) {
            vo.resetKey();
            if (maintainStatusName == null){
                break;
            }
            if (maintainStatusName.equals(vo.getItemValue())) {
                this.maintainStatus = (Integer) vo.getItemKey();
                break;
            }
        }
    }
    
}
