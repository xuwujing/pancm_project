package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseFaultType;

import java.util.List;
import java.util.Map;

/**
 * interface BaseFaultypeservice
 *
 * @author
 */
public interface IBaseFaultTypeService extends BaseService<BaseFaultType>{

    public Map<String,List<SelectVO>> listFaultTypeView();

     List<SelectVO> faultList();

}
