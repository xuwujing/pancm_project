package com.zans.portal.vo.home;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.HOME_RISK_TYPE_INDEX;

/**
 * @author xv
 * @since 2020/4/2 16:56
 */
@Builder
@Data
@Slf4j
public class RegionRiskVO {


    private Object region;

    @JSONField(name = "name")
    private String regionName;

    private Integer value;

    @JSONField(name = "risk_list")
    private Map<String, Integer> riskMap;

    public void increaseRiskCount(Object riskInput, Integer val) {
        if (!(riskInput instanceof Integer)) {
            return;
        }
        if (value == null) {
            value = 0;
        }
        if (riskMap == null) {
            riskMap = new HashMap<>(4);
        }
        Integer riskType = (Integer)riskInput;
        if (riskType >= HOME_RISK_TYPE_INDEX.length) {
            log.error("unknown risk type#{}", riskType);
            return;
        }
        riskMap.merge(HOME_RISK_TYPE_INDEX[riskType], val, Integer::sum);
        this.value += val;
    }
}
