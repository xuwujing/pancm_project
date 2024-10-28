package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author xv
 * @since 2020/4/28 18:00
 */
@Data
public class TaskRespVO {

    @JSONField(name = "")
    private Integer taskId;

    @JSONField(name = "")
    private String taskName;

    @JSONField(name = "")
    private Integer runCount;

    @JSONField(name = "")
    private Date runLastTime;

    @JSONField(name = "")
    private String remark;

    @JSONField(name = "")
    private Integer active;

    @JSONField(name = "")
    private Integer runMode;

    @JSONField(name = "")
    private String runTimeParams;

    @JSONField(name = "")
    private String runModule;

    @JSONField(name = "")
    private Integer runThreadNum;
}
