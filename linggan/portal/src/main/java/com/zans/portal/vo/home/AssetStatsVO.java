package com.zans.portal.vo.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.chart.PieChart;
import com.zans.portal.vo.stats.ArpHourStatVO;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class AssetStatsVO {

    private Integer total;

    @JSONField(name = "online")
    private Integer onlineCount;

    @JSONField(name = "chart_pie")
    private List<PieChart> chartPie;

    @JSONField(name = "chart_line")
    private List<ArpHourStatVO> chartLine;

}
