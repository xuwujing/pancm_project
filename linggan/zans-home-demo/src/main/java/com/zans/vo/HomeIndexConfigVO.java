package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
 * @author beixing
 * @Title: 首页指标表(HomeIndexConfig)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-10-22 15:02:52
 */
@Data
public class HomeIndexConfigVO implements Serializable {
    private static final long serialVersionUID = 805896869605364437L;

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

    private String templateText;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
