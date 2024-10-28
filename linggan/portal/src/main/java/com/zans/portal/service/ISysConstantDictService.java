package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysConstantDict;
import com.zans.portal.model.SysConstantItem;
import com.zans.portal.vo.constant.ConstantDictResVO;
import com.zans.portal.vo.constant.ConstantDictSearchVO;
import com.zans.portal.vo.constant.ConstantItemReqVO;
import com.zans.portal.vo.constant.ConstantItemSearchVO;

import java.util.List;
import java.util.Map;

public interface ISysConstantDictService extends BaseService<SysConstantDict> {

    PageResult<ConstantDictResVO> getConstantDictPage(ConstantDictSearchVO reqVO);

    SelectVO findByKeyOrName(String dictKey,String dictName);

    SysConstantDict findByKey(String dictKey);

}
