package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.SysConstantDictMapper;
import com.zans.portal.model.SysConstantDict;
import com.zans.portal.service.ISysConstantDictService;
import com.zans.portal.vo.constant.ConstantDictResVO;
import com.zans.portal.vo.constant.ConstantDictSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SysConstantDictServiceImpl extends BaseServiceImpl<SysConstantDict> implements ISysConstantDictService {

    SysConstantDictMapper constantDictMapper;

    @Resource
    public void setConstantDictMapper(SysConstantDictMapper constantDictMapper) {
        super.setBaseMapper(constantDictMapper);
        this.constantDictMapper = constantDictMapper;
    }

    @Override
    public PageResult<ConstantDictResVO> getConstantDictPage(ConstantDictSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<ConstantDictResVO> list = constantDictMapper.findConstantDictList(reqVO);
        return new PageResult<ConstantDictResVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public SelectVO findByKeyOrName(String dictKey, String dictName) {
        SelectVO selectVO = constantDictMapper.findByKeyOrName(dictKey, dictName);
        return selectVO;
    }

    @Override
    public SysConstantDict findByKey(String dictKey) {
        return constantDictMapper.findByKey(dictKey);
    }

}
