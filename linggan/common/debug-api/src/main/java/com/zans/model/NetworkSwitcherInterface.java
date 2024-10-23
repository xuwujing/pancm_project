package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (NetworkSwitcherInterface)实体类
 *
 * @author beixing
 * @since 2022-03-15 18:01:55
 */
@Data
@Table(name = "network_switcher_interface")
public class NetworkSwitcherInterface implements Serializable {
    private static final long serialVersionUID = 316660456964301071L;
    @Column(name = "id")
    private Integer id;
    /**
     * sys_switcher.id
     */
    @Column(name = "sw_id")
    private Integer swId;
    /**
     * 交换机IP
     */
    @Column(name = "sw_ip")
    private String swIp;
    /**
     * 接口编号
     */
    @Column(name = "interface_index")
    private Integer interfaceIndex;
    /**
     * 接口描述
     */
    @Column(name = "interface_detail")
    private String interfaceDetail;
    /**
     * MAC相关的物理接口编号，排除null、vlan等接口
     */
    @Column(name = "mac_index")
    private Integer macIndex;
    /**
     * 0,非物理型;1,物理型
     */
    @Column(name = "phy_status")
    private Integer phyStatus;
    /**
     * 端口状态：1up，2down
     */
    @Column(name = "up_status")
    private Object upStatus;
    /**
     * 接口mac地址
     */
    @Column(name = "mac")
    private String mac;
    /**
     * 接口类型：1up上行口，2down下行口;
     */
    @Column(name = "stream_type")
    private Object streamType;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 1 表示开启，这时从该接口学来的arp信息将算作资产和终端; 0表示未开启，过滤掉该接口学来的arp信息
     */
    @Column(name = "scan_on")
    private Integer scanOn;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
