package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (NetworkSwitcherInterface)实体类
 *
 * @author beixing
 * @since 2022-03-15 18:01:55
 */
@Data
public class NetworkSwitcherInterface implements Serializable {
    private static final long serialVersionUID = 193884057002316260L;
    private Integer id;
    /**
     * sys_switcher.id
     */
    private Integer swId;
    /**
     * 交换机IP
     */
    private String swIp;
    /**
     * 接口编号
     */
    private Integer interfaceIndex;
    /**
     * 接口描述
     */
    private String interfaceDetail;
    /**
     * MAC相关的物理接口编号，排除null、vlan等接口
     */
    private Integer macIndex;
    /**
     * 0,非物理型;1,物理型
     */
    private Integer phyStatus;
    /**
     * 端口状态：1up，2down
     */
    private Object upStatus;
    /**
     * 接口mac地址
     */
    private String mac;
    /**
     * 接口类型：1up上行口，2down下行口;
     */
    private Object streamType;
    private Date createTime;
    private Date updateTime;
    /**
     * 1 表示开启，这时从该接口学来的arp信息将算作资产和终端; 0表示未开启，过滤掉该接口学来的arp信息
     */
    private Integer scanOn;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
