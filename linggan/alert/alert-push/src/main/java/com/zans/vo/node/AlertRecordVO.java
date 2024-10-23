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

    private List<Map<String,Object>> mapList;

}
