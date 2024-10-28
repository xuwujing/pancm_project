package com.zans.mms.vo.asset;


import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ApiModel("资产信息诱导屏导入excle模板实体")
@Data
@ToString
public class ExcelGuidanceScreenAssetVO {

    @ApiModelProperty(value = "资产编号")
    @ExcelProperty(value = "资产编号", index = 0, validate = {"not_empty"})
    private String assetCode;

    @ApiModelProperty(value = "修改之后资产编号")
    @ExcelProperty(value = "修改之后资产编号", index = 1)
    private String assetCodeChange;

    @ApiModelProperty(value = "点位编号")
    @ExcelProperty(value = "点位编号", index = 2, validate = {"not_empty"})
    private String pointCode;


    @ApiModelProperty(value = "设备方位")
    @ExcelProperty(value = "设备方位", index = 3)
    private String deviceDirection;


    @ApiModelProperty(value = "设备功能")
    @ExcelProperty(value = "设备功能", index = 4)
    private String deviceSubType;

    @ApiModelProperty(value = "ip地址")
    @ExcelProperty(value = "ip地址", index = 5)
    private String networkIp;

    @ApiModelProperty(value = "mac地址")
    @ExcelProperty(value = "mac地址", index = 6)
    private String networkMac;

    @ApiModelProperty(value = "子网掩码")
    @ExcelProperty(value = "子网掩码", index = 7)
    private String networkMask;

    @ApiModelProperty(value = "网关")
    @ExcelProperty(value = "网关", index = 8)
    private String networkGeteway;

    @ApiModelProperty(value = "设备序列号")
    @ExcelProperty(value = "设备序列号", index = 9)
    private String deviceSn;

    @ApiModelProperty(value = "设备型号")
    @ExcelProperty(value = "设备型号", index = 10)
    private String deviceModelDes;

    @ApiModelProperty(value = "设备品牌")
    @ExcelProperty(value = "设备品牌", index = 11)
    private String deviceModelBrand;


    @ApiModelProperty(value = "软件版本")
    @ExcelProperty(value = "软件版本", index = 12)
    private String deviceSoftwareVersion;

    @ApiModelProperty(value = "项目名称")
    @ExcelProperty(value = "项目名称", index = 13)
    private String projectName;

    @ApiModelProperty(value = "建设年份")
    @ExcelProperty(value = "建设年份", index = 14)
    private String buildYear;

    @ApiModelProperty(value = "建设单位")
    @ExcelProperty(value = "建设单位", index = 15)
    private String buildCompany;

    @ApiModelProperty(value = "建设单位联系人")
    @ExcelProperty(value = "建设单位联系人", index = 16)
    private String buildContact;

    @ApiModelProperty(value = "建设单位联系电话")
    @ExcelProperty(value = "建设单位联系电话", index = 17)
    private String buildPhone;

    @ApiModelProperty(value = "运维单位")
    @ExcelProperty(value = "运维单位", index = 18)
    private String maintainCompany;

    @ApiModelProperty(value = "运维单位联系人")
    @ExcelProperty(value = "运维单位联系人", index = 19)
    private String maintainContact;

    @ApiModelProperty(value = "运维单位联系电话")
    @ExcelProperty(value = "运维单位联系电话", index = 20)
    private String maintainPhone;

    @ApiModelProperty(value = "检测方式")
    @ExcelProperty(value = "检测方式", index = 21)
    private String detectMode;

    @ApiModelProperty(value = "维护状态")
    @ExcelProperty(value = "维护状态", index = 22)
    private String maintainStatus;

    @ApiModelProperty(value = "设备账号")
    @ExcelProperty(value = "设备账号", index = 23)
    private String deviceAccount;

    @ApiModelProperty(value = "设备密码")
    @ExcelProperty(value = "设备密码", index = 24)
    private String devicePwd;

    @ApiModelProperty(value = "安装时间")
    @ExcelProperty(value = "安装时间", index = 25)
    private String installTime;


    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注", index = 26)
    private String remark;


    @ApiModelProperty(value = "负责单位")
    @ExcelProperty(value = "负责单位", index = 27)
    private String orgId;


    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    private String pointId;

    private String creator;

    private Date createTime;

    private Date updateTime;

}
