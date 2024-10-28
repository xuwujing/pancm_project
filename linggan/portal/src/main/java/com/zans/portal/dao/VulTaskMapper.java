package com.zans.portal.dao;

import com.zans.portal.model.VulTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface VulTaskMapper extends Mapper<VulTask> {

    List<String> getVulTaskIpAddr(@Param("policy") Integer policy, @Param("limit")Integer limit);

    VulTask getUniqueByIpAddr(String ipAddr);

    int deleteById(Long id);

    int deleteRetGetCountMore();

    VulTask getByJobId(String jobId);

    int deleteByIpAddr(String hostIp);

    int updateStatus(@Param("jobId") String jobId, @Param("ipAddr") String ipAddr, @Param("status") int status);
}