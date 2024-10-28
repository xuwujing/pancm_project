package com.zans.job.vo;

import com.zans.job.model.OpsJob;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/6 16:08
 */
@Data
public class JobInfo {

    private String jobName;

    private String jobClassName;

    private String jobGroupName;

    private String cronExpression;

    private String jobType;

    private Integer timeType;

    private Map<String, String> extra;

    public static JobInfo setJobInfo(OpsJob job) {
        JobInfo info = new JobInfo();
        Map extra = new HashMap();
        info.setJobName("scan#" + job.getJobId());
        info.setJobClassName(job.getSharding() == 1 ? "ShardingScanJob" : "SimpleScanJob");
        info.setJobGroupName("scan");
        info.setCronExpression(job.getCronExpression());
        extra.put("job_id", String.valueOf(job.getJobId()));
        info.setExtra(extra);
        return info;
    }

}
