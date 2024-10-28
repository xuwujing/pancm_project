package com.zans.portal.vo.version;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (SysVersionInfo)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:23
 */
@ApiModel(value = "SysVersionInfo", description = "")
@Data
public class SysVersionInfoVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 213164490075033458L;
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 服务所属ip
     */
    @ApiModelProperty(value = "服务所属ip")
    private String serverIp;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String serverUrl;
    @ApiModelProperty(value = "${column.comment}")
    private String createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}