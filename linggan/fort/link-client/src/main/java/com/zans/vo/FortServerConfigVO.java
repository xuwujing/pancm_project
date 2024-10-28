package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (FortServerConfig)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-28 14:17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FortServerConfigVO implements Serializable {
    private static final long serialVersionUID = 422123815503387089L;
    /**
     * 自增ID
     */

    private Integer serverId;
    /**
     * 主机名称
     */

    private String serverName;
    /**
     * ip
     */

    private String serverIp;
    /**
     * 端口
     */

    private String serverPort;
    /**
     * 账号
     */

    private String serverAccount;
    /**
     * 密码
     */

    private String serverPwd;
    /**
     * 启用状态0否，1是
     */

    private Integer serverEnable;
    /**
     * 创建时间
     */

    private String createTime;
    /**
     * 更新时间
     */

    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public FortServerConfigVO(String serverName) {
        this.serverName = serverName;
    }
}
