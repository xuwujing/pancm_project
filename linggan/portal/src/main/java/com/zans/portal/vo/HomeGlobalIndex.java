package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局配置指标关联表(HomeGlobalIndex)实体类
 *
 * @author beixing
 * @since 2021-10-22 11:40:45
 */
@Data
public class HomeGlobalIndex implements Serializable {
    private static final long serialVersionUID = 659070048217115161L;
    private Integer id;
    private Integer globalId;
    private Integer indexId;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
