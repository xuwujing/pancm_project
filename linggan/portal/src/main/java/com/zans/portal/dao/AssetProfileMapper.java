package com.zans.portal.dao;

import com.zans.portal.model.AssetProfile;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AssetProfileMapper extends Mapper<AssetProfile> {

     AssetProfile findByIdAddr(@Param("ipAddr") String ipAddr);

    int deleteById(Integer id);

    int deleteByIp(String ip);
}