package com.zans.mms.dao.guard;

import java.util.List;

import com.zans.mms.model.SysVersionInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysVersionInfoDao {
    
    void resetVerifySuccess();

    List<SysVersionInfo> queryAllInfo();

    void updateVerifySuccess(@Param("id") int id,@Param("verifySuccess") int verifySuccess);

    SysVersionInfo findByIpProjectName(@Param("serverIp") String serverIp, @Param("projectName") String projectName);

    List<SysVersionInfo> queryAllServerIp();

}
