package com.zans.portal.vo.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.chart.PieChart;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/4 18:45
 */
@Data
public class IpStatsVO {

    private Integer total;

    @JSONField(name = "chart_shape")
    private List<PieChart> data;
}
