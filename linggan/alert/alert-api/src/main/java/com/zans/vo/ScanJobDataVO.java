package com.zans.vo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author beixing
 * @Title: alert-api
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/15
 */
@Data
public class ScanJobDataVO {

    private Long strategyId;

    private String groupSql;

    private String alertThreshold;

    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
