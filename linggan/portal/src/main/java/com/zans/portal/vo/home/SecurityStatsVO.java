package com.zans.portal.vo.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.chart.CircleUnit;
import lombok.Data;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/5 9:54
 */
@Data
public class SecurityStatsVO {

    private Integer total;

    @JSONField(name = "safe_index")
    private Double safeIndex;

    @JSONField(name = "risk_private")
    private Integer riskPrivate;

    @JSONField(name = "risk_forge")
    private Integer riskForge;

    @JSONField(name = "risk_replace")
    private Integer riskReplace;

    @JSONField(name = "risk_exception")
    private Integer riskException;

    private List<CircleUnit> device;


}
