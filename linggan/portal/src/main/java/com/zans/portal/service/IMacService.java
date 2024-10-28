package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.TMac;
import com.zans.portal.vo.arp.ExcelMacVO;
import com.zans.portal.vo.mac.MacRespVO;
import com.zans.portal.vo.mac.MacVO;

import java.util.List;
import java.util.Map;

/**
 * @author yhj
 *
 */
public interface IMacService extends BaseService<TMac> {

    PageResult<MacRespVO> getMacPage(MacVO reqVO);

    int getByMacAddress(String macAddress, Integer id);

    int insertBatch(Map<String,Object> map);

    int batchUpdateCompany(Map<String,Object> map);

    List<MacRespVO> findMacListByMacAddr(Map<String,Object> map);

    List<ExcelMacVO> findMacList(MacVO reqVO);



}
