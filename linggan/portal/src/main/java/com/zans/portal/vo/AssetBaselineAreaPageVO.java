package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: (AssetBaselineArea)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-09-14 17:04:05
 */
@Data
public class AssetBaselineAreaPageVO extends BasePage implements Serializable {
    private static final long serialVersionUID = -60360109606192330L;
    private Integer id;
    /**
     * 父级节点
     */
    private Integer parentId;
    /**
     * 辖区名称
     */
    private String areaName;
    /**
     * ip地址起
     */
    private String ipSegBegin;
    /**
     * ip地址止
     */
    private String ipSegEnd;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * vlan
     */
    private String vlan;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}