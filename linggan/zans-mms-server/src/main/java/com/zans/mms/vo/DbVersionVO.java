package com.zans.mms.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.zans.base.vo.BasePage;

/**
 * @author beixing
 * @Title: (DbVersion)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:32
 */
@ApiModel(value = "DbVersion", description = "")
@Data
public class DbVersionVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 897500592533861945L;
    /**
     * 自增ID
     */
    @ApiModelProperty(value = "自增ID")
    private Long id;
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;
    /**
     * 升级说明
     */
    @ApiModelProperty(value = "升级说明")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
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
