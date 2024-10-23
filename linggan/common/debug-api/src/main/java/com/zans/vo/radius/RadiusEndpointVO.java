package com.zans.vo.radius;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author beixing
 * @Title: radius数据库 (RadiusEndpoint)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-01-14 15:55:14
 */
@Data
public class RadiusEndpointVO implements Serializable {
    private static final long serialVersionUID = -24329683126083747L;
    /**
     * id
     */
    private Integer id;
    /**
     * 终端mac地址，17位
     */
    private String username;
    /**
     * 12位
     */
    private String pass;
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
    private Date createTime;
    private Date updateTime;
    private String baseIp;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
