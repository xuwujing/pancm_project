package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.SysCustomFieldMapper;
import com.zans.portal.model.SysCustomField;
import com.zans.portal.service.ICustomFieldService;
import com.zans.portal.vo.custom.CustomReqVO;
import com.zans.portal.vo.custom.CustomRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yhj
 */
@Service
@Slf4j
public class CustomFiledServiceImpl extends BaseServiceImpl<SysCustomField> implements ICustomFieldService {

    SysCustomFieldMapper customFiledMapper;

    @Resource
    public void setCustomFiledMapper(SysCustomFieldMapper customFiledMapper) {
        super.setBaseMapper(customFiledMapper);
        this.customFiledMapper = customFiledMapper;
    }


    @Override
    public PageResult<CustomRespVO> getCustomFieldPage(CustomReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<CustomRespVO> list = customFiledMapper.findCustomFieldList(reqVO);
        return new PageResult<CustomRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<CustomRespVO> findCustomDataList(String id, String moduleName) {
        return customFiledMapper.findCustomDataList(id, moduleName);
    }

    @Override
    public Boolean updateCustomFieldInTable(String id, String tableName, String column, String value) {
        return customFiledMapper.updateCustomFieldInTable(id, tableName, column, value);
    }

    @Override
    public Integer getMaxSort(String moduleName) {
        return customFiledMapper.getMaxSort(moduleName);
    }

}
