package com.zans.portal.service.impl;

import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.portal.dao.AssetBaselineDao;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.vo.AssetBaselineVersionVO;
import com.zans.portal.model.AssetBaselineVersion;
import com.zans.portal.dao.AssetBaselineVersionDao;
import com.zans.portal.service.IAssetBaselineVersionService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author beixing
 * @Title: 基线变更记录表(AssetBaselineVersion)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:10:23
 */
@Service("assetBaselineVersionService")
public class AssetBaselineVersionServiceImpl extends BaseServiceImpl<AssetBaselineVersion>  implements IAssetBaselineVersionService {
    AssetBaselineVersionDao assetBaselineVersionDao;

    @Resource
    public void setAssetBaselineVersionDao(AssetBaselineVersionDao assetBaselineVersionDao) {
        super.setBaseMapper(assetBaselineVersionDao);
        this.assetBaselineVersionDao = assetBaselineVersionDao;
    }


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AssetBaselineVersionVO queryById(Long id) {
        return this.assetBaselineVersionDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(AssetBaselineVersionVO assetBaselineVersion) {
        int pageNum = assetBaselineVersion.getPageNum();
        int pageSize = assetBaselineVersion.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AssetBaselineVersionVO> result = assetBaselineVersionDao.queryAll(assetBaselineVersion);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));

    }

    /**
     * 新增数据
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(AssetBaselineVersionVO assetBaselineVersionVO) {
        AssetBaselineVersion assetBaselineVersion = new AssetBaselineVersion();
        BeanUtils.copyProperties(assetBaselineVersionVO, assetBaselineVersion);
        return assetBaselineVersionDao.insert(assetBaselineVersion);
    }

    /**
     * 修改数据
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(AssetBaselineVersionVO assetBaselineVersionVO) {
        AssetBaselineVersion assetBaselineVersion = new AssetBaselineVersion();
        BeanUtils.copyProperties(assetBaselineVersionVO, assetBaselineVersion);
        return assetBaselineVersionDao.update(assetBaselineVersion);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.assetBaselineVersionDao.deleteById(id) > 0;
    }


}
