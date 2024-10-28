package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.TMacMapper;
import com.zans.portal.model.TMac;
import com.zans.portal.service.IMacService;
import com.zans.portal.vo.arp.ExcelMacVO;
import com.zans.portal.vo.mac.MacRespVO;
import com.zans.portal.vo.mac.MacVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yhj
 */
@Service
@Slf4j
public class MacServiceImpl extends BaseServiceImpl<TMac> implements IMacService {

    TMacMapper macMapper;

    @Resource
    public void setMacMapper(TMacMapper macMapper) {
        super.setBaseMapper(macMapper);
        this.macMapper = macMapper;
    }

    @Override
    public PageResult<MacRespVO> getMacPage(MacVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<MacRespVO> list = macMapper.findMacList(reqVO);
        return new PageResult<MacRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public int getByMacAddress(String macAddress, Integer id) {
        return macMapper.getByMacAddress(macAddress, id);
    }

    @Override
    public int insertBatch(Map<String, Object> map) {
        return macMapper.insertBatch(map);
    }

    @Override
    public int batchUpdateCompany(Map<String,Object> map) {
        return macMapper.batchUpdateCompany(map);
    }

    @Override
    public List<MacRespVO> findMacListByMacAddr(Map<String, Object> map) {
        return macMapper.findMacListByMacAddr(map);
    }

    @Override
    public List<ExcelMacVO> findMacList(MacVO reqVO) {
        List<MacRespVO> list = macMapper.findMacList(reqVO);
        List<ExcelMacVO> excelMacVOList = new ArrayList<>();
        list.forEach(macRespVO -> {
            ExcelMacVO excelMacVO = new ExcelMacVO();
            BeanUtils.copyProperties(macRespVO,excelMacVO);
            excelMacVOList.add(excelMacVO);
        });
        return excelMacVOList;
    }

}
