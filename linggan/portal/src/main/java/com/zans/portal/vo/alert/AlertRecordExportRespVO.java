package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;


@Data
public class AlertRecordExportRespVO {





    @ExcelProperty( value = "ip地址",index = 0, validate = {"not_empty"}, width = 20)
    private String ipAddr;

    @ExcelProperty( value = "点位名称",index = 1, validate = {"not_empty"}, width = 30)
    private String pointName;

    @ExcelProperty( value = "MAC地址",index = 2, validate = {"not_empty"}, width = 15)
    private String mac;

    @ExcelProperty( value = "告警类型",index = 3, validate = {"not_empty"}, width = 15)
    private String typeName;

    @ExcelProperty( value = "规则名称",index = 4, validate = {"not_empty"}, width = 15)
    private String strategyName;

    @ExcelProperty( value = "策略名称",index = 5, validate = {"not_empty"}, width = 15)
    private String groupName;

    @ExcelProperty( value = "等级",index = 6, validate = {"not_empty"} )
    private String alertLevelName;

    @ExcelProperty( value = "告警内容",index = 7, validate = {"not_empty"}, width = 30)
    private String noticeInfo;

    @ExcelProperty( value = "处理状态",index = 8, validate = {"not_empty"}, width = 15)
    private String dealName;

    @ExcelProperty( value = "告警时间",index = 9, validate = {"not_empty"}, width = 20)
    private String noticeTime;

    @ExcelProperty( value = "恢复状态",index = 8, validate = {"not_empty"}, width = 15)
    private String recoverName;

    @ExcelProperty( value = "恢复时间",index = 9, validate = {"not_empty"}, width = 20)
    private String recoverTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
