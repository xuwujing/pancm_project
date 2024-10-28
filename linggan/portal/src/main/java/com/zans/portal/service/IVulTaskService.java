package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.VulTask;

import java.util.List;

public interface IVulTaskService extends BaseService<VulTask> {
    List<String> getVulTaskIpAddr(Integer policy, Integer limit);

    Boolean addVulTask(String ipAddr, Integer policy, Boolean priorityFlag);

    int deleteRetGetCountMore();

    Boolean getVulTaskRet(String jobId, Integer policy);
}
