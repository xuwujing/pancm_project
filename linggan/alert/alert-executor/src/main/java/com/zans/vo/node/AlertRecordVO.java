package com.zans.vo.node;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: alert-executor
 * @Description: 告警内容
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/14
 */
@Data
public class AlertRecordVO {

    private  Long ruleId;

    /** 数据状态 0 故障，1，恢复   */
    private Integer status;

    /** 模板启用状态 0 否 (使用sql查询模板)，1是(使用下面map的数据内容) */
    private Integer templateEnable;

    private List<Map<String,Object>> mapList;

}
