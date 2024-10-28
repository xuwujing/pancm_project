package com.zans.portal.vo.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.stats.IpDayStatVO;
import lombok.Data;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/4 19:32
 */
@Data
public class AuthStatsVO {

    private Integer total;

    private Integer verified;

    private Integer review;

    private Integer refuse;

    @JSONField(name = "chart_circle")
    private List<CircleUnit> chartCircle;

    @JSONField(name = "chart_line")
    private List<IpDayStatVO> chartLine;
}
