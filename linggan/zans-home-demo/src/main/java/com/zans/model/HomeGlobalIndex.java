package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 全局配置指标关联表(HomeGlobalIndex)实体类
 *
 * @author beixing
 * @since 2021-10-22 11:40:45
 */
@Data
@Table(name = "home_global_index")
public class HomeGlobalIndex implements Serializable {
    private static final long serialVersionUID = 390409720023021753L;
    @Column(name = "id")
    private Integer id;
    @Column(name = "global_id")
    private Integer globalId;
    @Column(name = "index_id")
    private Integer indexId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
