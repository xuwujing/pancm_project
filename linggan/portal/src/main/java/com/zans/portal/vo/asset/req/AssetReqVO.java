package com.zans.portal.vo.asset.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description="资产地图请求参数")
@Data
public class AssetReqVO extends BasePage {

    @JSONField(name = "id")
    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @JSONField(name = "ip_addr")
    @ApiModelProperty(name = "ip_addr", value = "ip地址")
    private String ipAddr;

    @JSONField(name = "longitude")
    @ApiModelProperty(name = "longitude", value = "经度",hidden = true)
    private String longitude;

    @JSONField(name = "latitude")
    @ApiModelProperty(name = "latitude", value = "纬度",hidden = true)
    private String latitude;

    @JSONField(name = "contractor_person")
    @ApiModelProperty(name = "contractor_person", value = "联系人",hidden = true)
    private String contractorPerson;

    @JSONField(name = "project_name")
    @ApiModelProperty(name = "project_name", value = "项目名称",hidden = true)
    private String projectName;

    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护单位",hidden = true)
    private String maintainCompany;

    @JSONField(name = "point_name")
    @ApiModelProperty(name = "point_name", value = "点位名称")
    private String pointName;

    @JSONField(name = "brand_name")
    @ApiModelProperty(name = "brand_name", value = "名牌",hidden = true)
    private String brandName;


    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;


    @ApiModelProperty(name = "department_id", value = "部门id",hidden = true)
    private Integer departmentId;

    @JSONField(name = "department_name")
    @ApiModelProperty(name = "department_name", value = "部门name",hidden = true)
    private String departmentName;


    @JSONField(name = "device_type")
    @ApiModelProperty(name = "device_type", value = "设备类型id",hidden = true)
    private Integer deviceType;

    @JSONField(name = "type_name")
    @ApiModelProperty(name = "type_name", value = "设备类型名称",hidden = true)
    private String typeName;

    @ApiModelProperty(name = "deviceStatus", value = "设备状态;-1 未知;0.阻断;1.在线;2.离线;3.告警;",hidden = true)
    private String deviceStatus;

    @ApiModelProperty(name = "buildTypes", value = "海康=1期  广电 1; 其他二期  电信 2")
    private List<String> buildTypes;

    @ApiModelProperty(name = "brandNameIsNoHaiKang", value = "海康=1期  广电 1; 其他二期  电信 2",hidden = true)
    private String brandNameIsNoHaiKang;

    @ApiModelProperty(name = "unknownDevice", value = "未知设备 有ip没有经纬度,查询未知设备时候非空")
    private String unknownDevice;

    @JSONField(name = "alive")
    @ApiModelProperty(name = "alive", value = "1.在线;2.离线")
    private Integer alive;
}
