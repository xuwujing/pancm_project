package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.portal.model.TDeviceModelScan;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.model.ModelScanSearchVO;
import com.zans.portal.vo.model.ModelScanRespVO;

public interface IModelScanService extends BaseService<TDeviceModelScan> {

    PageResult<ModelScanRespVO> getModelPage(ModelScanSearchVO req);

    ModelScanRespVO getModelById(Integer id);
}
