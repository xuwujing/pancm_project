package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "asset_profile")
public class AssetProfile implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "ip_addr")
    private String ipAddr;



    /**
     * 当前MAC地址
     */
    @Column(name = "cur_mac")
    private String curMac;

    /**
     * 当前公司名称
     */
    @Column(name = "cur_company")
    private String curCompany;

    /**
     * 设备品牌
     */
    @Column(name = "cur_brand")
    private String curBrand;

    /**
     * 当前设备类型
     */
    @Column(name = "cur_device_type")
    private Integer curDeviceType;

    /**
     * 如果设备型号有改变，记录新的设备信号
     */
    @Column(name = "cur_model_des")
    private String curModelDes;

    /**
     * 是否哑终端 0：活跃终端；1：哑终端
     */
    @Column(name = "cur_mute")
    private Integer curMute;

    /**
     * 服务器操作系统版本
     */
    @Column(name = "cur_server_os")
    private String curServerOs;

    /**
     * 交换机ip
     */
    @Column(name = "cur_sw_ip")
    private String curSwIp;

    /**
     * 备注
     */
    private String remark;

    /**
     * 录入人(创建人)
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 创建人
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private Integer updateId;

    @Column(name = "source")
    private Integer source;

    @Column(name = "delete_status")
    private Integer deleteStatus;

    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


    /**
     * 当前开放端口
     */
    @Column(name = "cur_open_port")
    private String curOpenPort;

    private static final long serialVersionUID = 1L;



    @Override
    public String toString() {

        return JSONObject.toJSONString(this);
    }
}
