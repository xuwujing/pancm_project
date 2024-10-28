package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.TDeviceModelScanMapper;
import com.zans.portal.model.TDeviceModelScan;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.IModelScanService;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.model.ModelScanSearchVO;
import com.zans.portal.vo.model.ModelScanRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ModelScanServiceImpl extends BaseServiceImpl<TDeviceModelScan> implements IModelScanService {

    TDeviceModelScanMapper modelScanMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Resource
    public void setModelScanMapper(TDeviceModelScanMapper modelScanMapper) {
        super.setBaseMapper(modelScanMapper);
        this.modelScanMapper = modelScanMapper;
    }

    @Override
    public PageResult<ModelScanRespVO> getModelPage(ModelScanSearchVO asset) {
        int pageNum = asset.getPageNum();
        int pageSize = asset.getPageSize();

        Map<Object, String> confirmMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_MODEL_SCAN_CONFIRM);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ARP_MUTE);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_MODEL_SCAN_SOURCE);

        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ModelScanRespVO> list = modelScanMapper.findModelList(asset);
        for (ModelScanRespVO row : list) {
            row.setSourceByMap(sourceMap);
            row.setConfirmByMap(confirmMap);
            row.setMuteByMap(muteMap);
        }
        return new PageResult<ModelScanRespVO>(page.getTotal(), page.getResult(), pageSize, pageNum);
    }

    @Override
    public ModelScanRespVO getModelById(Integer id) {
        ModelScanRespVO data = modelScanMapper.getModelById(id);
        if (data == null) {
            return null;
        }

        Map<Object, String> confirmMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_MODEL_SCAN_CONFIRM);
        Map<Object, String> muteMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ARP_MUTE);
        Map<Object, String> sourceMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_MODEL_SCAN_SOURCE);
        data.setConfirmByMap(confirmMap);
        data.setMuteByMap(muteMap);
        data.setSourceByMap(sourceMap);
        return data;
    }
}
