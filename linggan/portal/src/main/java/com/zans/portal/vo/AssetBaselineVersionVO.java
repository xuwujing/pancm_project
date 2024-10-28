package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 基线变更记录表(AssetBaselineVersion)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:10:23
 */
@ApiModel(value = "AssetBaselineVersion", description = "基线变更记录表")
@Data
public class AssetBaselineVersionVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -85457359522021858L;
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * asset_baseline.id
     */
    @ApiModelProperty(value = "asset_baseline.id")
    private Long assetBaselineId;
    /**
     * 终端mac地址
     */
    @ApiModelProperty(value = "终端mac地址")
    private String mac;
    /**
     * base_ip_mac的ip_addr
     */
    @ApiModelProperty(value = "base_ip_mac的ip_addr")
    private String ipAddr;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_model_des")
    private String modelDes;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brandName;
    /**
     * endpoint_profile放行时候的cur_device_type
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_device_type")
    private Integer deviceType;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_server_os")
    private String serverOs;
    /**
     * nas_ip
     */
    @ApiModelProperty(value = "nas_ip")
    private String nasIp;
    /**
     * nas_port_id
     */
    @ApiModelProperty(value = "nas_port_id")
    private String nasPortId;
    /**
     * 虚拟局域网网络
     */
    @ApiModelProperty(value = "虚拟局域网网络")
    private String vlan;
    /**
     * 掩码
     */
    @ApiModelProperty(value = "掩码")
    private String mask;
    /**
     * 网关
     */
    @ApiModelProperty(value = "网关")
    private String gateway;
    /**
     * 点位名称
     */
    @ApiModelProperty(value = "点位名称")
    private String pointName;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double latitude;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 承建单位
     */
    @ApiModelProperty(value = "承建单位")
    private String contractor;
    /**
     * 项目状态：0建设中，1质保，2过保，3维保
     */
    @ApiModelProperty(value = "项目状态：0建设中，1质保，2过保，3维保")
    private Integer projectStatus;
    /**
     * 承建单位联系人
     */
    @ApiModelProperty(value = "承建单位联系人")
    private String contractorPerson;
    /**
     * 承建单位联系电话
     */
    @ApiModelProperty(value = "承建单位联系电话")
    private String contractorPhone;
    /**
     * 维护单位
     */
    @ApiModelProperty(value = "维护单位")
    private String maintainCompany;
    /**
     * 维护联系人
     */
    @ApiModelProperty(value = "维护联系人")
    private String maintainPerson;
    /**
     * 维护联系电话
     */
    @ApiModelProperty(value = "维护联系电话")
    private String maintainPhone;
    /**
     * map_catalogue.id
     */
    @ApiModelProperty(value = "map_catalogue.id")
    private Integer mapCatalogueId;
    /**
     * 变更意见
     */
    @ApiModelProperty(value = "变更意见")
    private String remark;
    /**
     * 本次创建人
     */
    @ApiModelProperty(value = "本次创建人")
    private String creator;
    /**
     * 历史记录创建人
     */
    @ApiModelProperty(value = "历史记录创建人")
    private String versionCreator;
    /**
     * 历史记录创建基线时间
     */
    @ApiModelProperty(value = "历史记录创建基线时间")
    private String versionCreateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "开放端口")
    private String openPort;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
