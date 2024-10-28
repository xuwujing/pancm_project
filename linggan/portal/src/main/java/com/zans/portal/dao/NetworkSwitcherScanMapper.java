package com.zans.portal.dao;

import com.zans.portal.model.NetworkSwitcherScan;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface NetworkSwitcherScanMapper extends Mapper<NetworkSwitcherScan> {

    /**
     * 查找IP对应的交换机最后一条扫描记录
     */
    NetworkSwitcherScan  findLastScanRecordByIp(@Param("ip") String ip);
}