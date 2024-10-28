package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 首页全局配置表(HomeGlobalConfig)实体类
 *
 * @author beixing
 * @since 2021-10-21 10:37:07
 */
@Data
@Table(name = "home_global_config")
public class HomeGlobalConfig implements Serializable {
    private static final long serialVersionUID = 239028237810867665L;
    @Column(name = "id")
    private Integer id;
    /**
     * 首页布局的坐标样式
     */
    @Column(name = "coord_data")
    private String coordData;
    /**
     * 首页的指标选择,需要和布局对应
     */
    @Column(name = "index_data")
    private String indexData;
    /**
     * 数量
     */
    @Column(name = "count")
    private Integer count;
    /**
     * 0:禁用,1:启用
     */
    @Column(name = "enable")
    private Integer enable;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
