package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (RadiusEndpoint)实体类
 *
 * @author beixing
 * @since 2022-03-16 10:52:38
 */
@Data
public class RadiusEndpoint implements Serializable {
    private static final long serialVersionUID = 361624094243138694L;
    /**
     * id
     */
    private Integer id;
    /**
     * 12位mac地址
     */
    private String mac;
    /**
     * 基线IP
     */
    private String baseIp;
    /**
     * 0, 阻断；1，检疫区，2，放行
     */
    private Integer accessPolicy;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 审批人
     */
    private String authPerson;
    /**
     * 审核意见
     */
    private String authMark;
    /**
     * 审核时间
     */
    private String authTime;
    /**
     * 1, Radius写入；2，系统批量导入，3，前台写入
     */
    private Integer insertType;
    /**
     * 0, 正常； 1，删除
     */
    private Integer deleteStatus;
    /**
     * 0，停用;1,启用
     */
    private Integer enableStatus;
    /**
     * 1.系统-arp with radius;2.系统arp without radius; 3.系统vpn; 4. 人工; 5.导入
     */
    private Integer source;
    /**
     * 创建人,系统录入默认admin
     */
    private String createPerson;
    /**
     * 第三方id
     */
    private String otherUuid;
    /**
     * 第三方扫描的状态,扫描过的状态为1
     */
    private Integer otherScanStatus;
    /**
     * 第三方扫描的最新时间
     */
    private String otherScanTime;
    /**
     * 检疫设备状态 0,新设备;1,基线设备
     */
    private Integer qzDeviceStatus;
    private Date createTime;
    private Date updateTime;
    private Integer revealStatus;
    /**
     * 白名单状态,0:否,1:是
     */
    private Integer whiteStatus;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
