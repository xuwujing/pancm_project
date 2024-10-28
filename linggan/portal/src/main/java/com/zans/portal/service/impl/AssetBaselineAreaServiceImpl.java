package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.portal.dao.AssetBaselineAreaDao;
import com.zans.portal.model.AssetBaselineArea;
import com.zans.portal.service.IAssetBaselineAreaService;
import com.zans.portal.vo.AssetBaselineAreaPageVO;
import com.zans.portal.vo.AssetBaselineAreaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * create by: beiming
 * create time: 2021/11/15 17:30
 */
@Service("assetBaselineAreaService")
@Slf4j
public class AssetBaselineAreaServiceImpl extends BaseServiceImpl<AssetBaselineArea> implements IAssetBaselineAreaService {
    private AssetBaselineAreaDao assetBaselineAreaDao;

    @Resource
    public void setAssetBaselineAreaDao(AssetBaselineAreaDao assetBaselineAreaDao) {
        super.setBaseMapper(assetBaselineAreaDao);
        this.assetBaselineAreaDao = assetBaselineAreaDao;
    }

    @Override
    public AssetBaselineArea getByName(String areaName) {
        return assetBaselineAreaDao.getByName(areaName);
    }

    @Override
    public int deleteById(Integer id) {
        return assetBaselineAreaDao.deleteById(id);
    }

    @Override
    public ApiResult list(AssetBaselineAreaPageVO vo) {
        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AssetBaselineAreaVO> result = assetBaselineAreaDao.queryAll(vo);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));

    }

    @Override
    public ApiResult listAll() {
        List<AssetBaselineAreaVO> list = assetBaselineAreaDao.queryAll(null);
        return ApiResult.success(list);
    }

    @Override
    public AssetBaselineArea getOnlyLevel() {
        return assetBaselineAreaDao.getOnlyLevel();
    }

    @Override
    public int updateById(AssetBaselineArea assetBaselineArea) {
        return assetBaselineAreaDao.update(assetBaselineArea);
    }
}
