package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "FortServerConfig", description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FortServerConfigVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 422123815503387089L;
    /**
     * 自增ID
     */
    @ApiModelProperty(value = "自增ID")
    private Integer serverId;
    /**
     * 主机名称
     */
    @ApiModelProperty(value = "主机名称")
    private String serverName;
    /**
     * ip
     */
    @ApiModelProperty(value = "ip")
    private String serverIp;
    /**
     * 端口
     */
    @ApiModelProperty(value = "端口")
    private String serverPort;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String serverAccount;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String serverPwd;
    /**
     * 启用状态0否，1是
     */
    @ApiModelProperty(value = "启用状态0否，1是")
    private Integer serverEnable;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public FortServerConfigVO(String serverName) {
        this.serverName = serverName;
    }
}
