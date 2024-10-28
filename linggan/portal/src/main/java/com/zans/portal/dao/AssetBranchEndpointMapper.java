package com.zans.portal.dao;

import com.zans.portal.model.AssetBranchEndpoint;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface AssetBranchEndpointMapper extends Mapper<AssetBranchEndpoint> {
    AssetBranchEndpoint getByUsername(@Param("username") String username, @Param("assetBranchId") Integer assetBranchId);
}