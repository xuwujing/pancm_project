package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetAddReqVO", description = "")
@Data
public class AssetAddReqVO {


    /**
     * 资产编号
     */
    private String assetCode;

    /**
     * 设备方位
     */
    private String deviceDirection;

    /**
     * 点位ID
     */
    private Long pointId;

    /**
     * 设备类型id
     */
    private String deviceType;

    /**
     * 设备类型子类 非字典，直接录入文字
     */
    private String deviceSubType;

    /**
     * ip地址
     */
    private String networkIp;

    /**
     * mac地址 12位，去空格
     */
    private String networkMac;

    /**
     * 子网掩码
     */
    private String networkMask;

    /**
     * 网关
     */
    private String networkGeteway;

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 设备型号
     */
    private String deviceModelDes;

    /**
     * 设备品牌
     */
    private String deviceModelBrand;

    /**
     * 软件版本
     */
    private String deviceSoftwareVersion;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 建设年份
     */
    private String buildYear;

    /**
     * 建设单位
     */
    private String buildCompany;

    /**
     * 建设单位联系人
     */
    private String buildContact;

    /**
     * 建设单位联系电话
     */
    private String buildPhone;

    /**
     * 运维单位名称
     */
    private String maintainCompany;

    /**
     * 运维单位名称
     */
    private String maintainContact;

    /**
     * 运维单位名称
     */
    private String maintainPhone;

    /**
     * 检测方式 视频、雷达、线圈
     */
    private String detectMode;

    /**
     * 设备维护状态
     */
    private String maintainStatus;

    /**
     * 设备账号 联网设备有该属性
     */
    private String deviceAccount;

    /**
     * 设备账号 设备密码
     */
    private String devicePwd;

    /**
     * 在线 离线
     */
    private String onlineStatus;

    /**
     * 断光 断电
     */
    private String offlineStatus;

    /**
     * 相机安装时间
     */
    private String installTime;

    /**
     * 车道数
     */
    private Integer laneNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String creator;
}
