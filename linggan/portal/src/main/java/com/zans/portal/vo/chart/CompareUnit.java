package com.zans.portal.vo.chart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * GroupUnit
 * @author xv
 * @since 2020-03-04
 */
@Data
@Slf4j
public class CompareUnit {

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "success")
    private Object success;

    @JSONField(name = "failed")
    private Object failed;


}
