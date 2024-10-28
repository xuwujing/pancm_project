package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页全局配置表(HomeGlobalConfig)实体类
 *
 * @author beixing
 * @since 2021-10-21 10:37:02
 */
@Data
public class HomeGlobalConfigVo implements Serializable {
    private static final long serialVersionUID = 314006669516347093L;
    private Integer id;
    

    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;


    private String title;
    private String uri;
    private String type;
    private Integer i;
    private Integer isChart;
    private Integer isTitle;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
