package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.portal.model.AssetLocalTrack;
import com.zans.portal.dao.AssetLocalTrackDao;
import com.zans.portal.service.IAssetLocalTrackService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 资产轨迹表(AssetLocalTrack)表服务实现类
 *
 * @author beixing
 * @since 2022-06-10 17:59:57
 */
@Service("assetLocalTrackService")
public class AssetLocalTrackServiceImpl implements IAssetLocalTrackService {
    @Resource
    private AssetLocalTrackDao assetLocalTrackDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AssetLocalTrack queryById(Integer id) {
        return this.assetLocalTrackDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(AssetLocalTrack assetLocalTrack) {
//        int pageNum = assetLocalTrack.getPageNum();
//        int pageSize = assetLocalTrack.getPageSize();
        int pageNum = 1;
        int pageSize = 1000;
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<AssetLocalTrack> result = assetLocalTrackDao.queryAll(assetLocalTrack);

        return ApiResult.success(new PageResult<AssetLocalTrack>(page.getTotal(), result, pageNum, pageSize));

    }

    /**
     * 新增数据
     *
     * @param assetLocalTrack 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(AssetLocalTrack assetLocalTrack) {
        return assetLocalTrackDao.insert(assetLocalTrack);
    }

    /**
     * 修改数据
     *
     * @param assetLocalTrack 实例对象
     * @return 实例对象
     */
    @Override
    public int update(AssetLocalTrack assetLocalTrack) {
        return assetLocalTrackDao.update(assetLocalTrack);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.assetLocalTrackDao.deleteById(id) > 0;
    }


    @Override
    public ApiResult view(AssetLocalTrack assetLocalTrack) {
        JSONObject jsonObject = assetLocalTrackDao.getGroupId(assetLocalTrack.getGroupId());
        return ApiResult.success(jsonObject);
    }
}
