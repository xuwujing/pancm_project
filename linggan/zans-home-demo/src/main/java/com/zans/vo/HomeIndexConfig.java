package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页指标表(HomeIndexConfig)实体类
 *
 * @author beixing
 * @since 2021-10-22 15:24:07
 */
@Data
public class HomeIndexConfig implements Serializable {
    private static final long serialVersionUID = 370642533210033651L;
    private Integer id;
    /**
     * 标题名称
     */
    private String title;
    /**
     * 指标类型名称(pie饼图，line折线图，bar柱状图)
     */
    private String type;
    /**
     * 数据集
     */
    private String dataList;
    /**
     * 请求接口的uri地址
     */
    private String uri;
    /**
     * 0:禁用,1:启用
     */
    private Integer enable;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
