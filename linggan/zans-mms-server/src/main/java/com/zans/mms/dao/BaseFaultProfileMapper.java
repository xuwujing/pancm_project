package com.zans.mms.dao;

import com.zans.mms.model.BaseFaultProfile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface BaseFaultProfileMapper extends Mapper<BaseFaultProfile> {

	Integer selectFaultId(@Param("deviceType") String deviceType,@Param("remark") String issueType);
}