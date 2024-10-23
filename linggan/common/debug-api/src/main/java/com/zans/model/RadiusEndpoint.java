package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (RadiusEndpoint)实体类
 *
 * @author beixing
 * @since 2022-03-16 10:52:39
 */
@Data
@Table(name = "radius_endpoint")
public class RadiusEndpoint implements Serializable {
    private static final long serialVersionUID = 937997885411536047L;
    /**
     * id
     */
    @Column(name = "id")
    private Integer id;
    /**
     * 12位mac地址
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 基线IP
     */
    @Column(name = "base_ip")
    private String baseIp;
    /**
     * 0, 阻断；1，检疫区，2，放行
     */
    @Column(name = "access_policy")
    private Integer accessPolicy;
    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 审批人
     */
    @Column(name = "auth_person")
    private String authPerson;
    /**
     * 审核意见
     */
    @Column(name = "auth_mark")
    private String authMark;
    /**
     * 审核时间
     */
    @Column(name = "auth_time")
    private String authTime;
    /**
     * 1, Radius写入；2，系统批量导入，3，前台写入
     */
    @Column(name = "insert_type")
    private Integer insertType;
    /**
     * 0, 正常； 1，删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;
    /**
     * 0，停用;1,启用
     */
    @Column(name = "enable_status")
    private Integer enableStatus;
    /**
     * 1.系统-arp with radius;2.系统arp without radius; 3.系统vpn; 4. 人工; 5.导入
     */
    @Column(name = "source")
    private Integer source;
    /**
     * 创建人,系统录入默认admin
     */
    @Column(name = "create_person")
    private String createPerson;
    /**
     * 第三方id
     */
    @Column(name = "other_uuid")
    private String otherUuid;
    /**
     * 第三方扫描的状态,扫描过的状态为1
     */
    @Column(name = "other_scan_status")
    private Integer otherScanStatus;
    /**
     * 第三方扫描的最新时间
     */
    @Column(name = "other_scan_time")
    private String otherScanTime;
    /**
     * 检疫设备状态 0,新设备;1,基线设备
     */
    @Column(name = "qz_device_status")
    private Integer qzDeviceStatus;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "reveal_status")
    private Integer revealStatus;
    /**
     * 白名单状态,0:否,1:是
     */
    @Column(name = "white_status")
    private Integer whiteStatus;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
