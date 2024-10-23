package com.zans.pojo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author pancm
 * @Title: alert-executor
 * @Description: 聚合记录的实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/19
 */
@Data
public class AlertRuleOriginalBean extends  AlertRecordBean{

    /** 关键字 */
    private String  keyword;

    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
