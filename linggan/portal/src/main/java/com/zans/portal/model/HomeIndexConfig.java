package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 首页指标表(HomeIndexConfig)实体类
 *
 * @author beixing
 * @since 2021-10-22 15:24:09
 */
@Data
@Table(name = "home_index_config")
public class HomeIndexConfig implements Serializable {
    private static final long serialVersionUID = -24579450366707743L;
    @Column(name = "id")
    private Integer id;
    /**
     * 标题名称
     */
    @Column(name = "title")
    private String title;
    /**
     * 指标类型名称(pie饼图，line折线图，bar柱状图)
     */
    @Column(name = "type")
    private String type;
    /**
     * 数据集
     */
    @Column(name = "data_list")
    private String dataList;
    /**
     * 请求接口的uri地址
     */
    @Column(name = "uri")
    private String uri;
    /**
     * 0:禁用,1:启用
     */
    @Column(name = "enable")
    private Integer enable;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
