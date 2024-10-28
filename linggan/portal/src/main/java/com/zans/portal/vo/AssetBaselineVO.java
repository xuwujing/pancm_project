package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:39
 */
@ApiModel(value = "AssetBaseline", description = "基线表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetBaselineVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 855698942672698570L;
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * endpoint_profile放行时候的cur_ip_addr
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_ip_addr")
    private String ipAddr;
    /**
     * 终端mac地址
     */
    @ApiModelProperty(value = "终端mac地址")
    private String mac;
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

    private String deviceTypeName;
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
     * 点位名称
     */
    @ApiModelProperty(value = "点位名称")
    private String pointName;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;
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

    private String projectStatusName;
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
     * 0:ip、mac未绑定，mac为空;1:已绑定
     */
    @ApiModelProperty(value = "0:ip、mac未绑定，mac为空;1:已绑定")
    private Integer bindStatus;
    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String reviser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;



    @ApiModelProperty(value = "开放端口")
    private String openPort;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    @ApiModelProperty(value = "是否哑终端")
    private Integer mute;

    /**
     * 维护状态(1正常,2迁改停用,3审核停用)
     */
    @ApiModelProperty(value = "维护状态")
    private String maintainStatus;


    private Integer parentId;
    private Integer assetBranchId;

    private Integer seq;

    private Integer deviceStatus;
    @ApiModelProperty(value = "是否存在资产id ,0不存在资产id")
    private Integer existAssetId;

    private String typeName;

    private String maintainStatusName;

    /**
     * 1-导出基线excel后删除基线数据; 0-只导出基线excel
     */
    private Integer deleteBaselineFlag;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public AssetBaselineVO(String ipAddr, String mac, String nasIp, String vlan) {
        this.ipAddr = ipAddr;
        this.mac = mac;
        this.nasIp = nasIp;
        this.vlan = vlan;
    }
}
