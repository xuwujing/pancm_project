package com.zans.service.impl;


import com.zans.dao.HikFeatureDao;
import com.zans.model.HikFeature;
import com.zans.service.IHikFeatureService;
import com.zans.vo.ApiResult;
import com.zans.vo.HikFeatureVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author beixing
 * @Title: (HikFeature)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-14 11:04:04
 */
@Service("hikFeatureService")
public class HikFeatureServiceImpl implements IHikFeatureService {
    @Resource
    private HikFeatureDao hikFeatureDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public HikFeatureVO queryById(Integer id) {
        return this.hikFeatureDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(HikFeatureVO hikFeature) {
        List<HikFeatureVO> result = hikFeatureDao.queryAll(hikFeature);
        return ApiResult.success(result);

    }

    /**
     * 新增数据
     *
     * @param hikFeatureVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(HikFeatureVO hikFeatureVO) {
        HikFeature hikFeature = new HikFeature();
        BeanUtils.copyProperties(hikFeatureVO, hikFeature);
        return hikFeatureDao.insert(hikFeature);
    }

    /**
     * 修改数据
     *
     * @param hikFeatureVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(HikFeatureVO hikFeatureVO) {
        HikFeature hikFeature = new HikFeature();
        BeanUtils.copyProperties(hikFeatureVO, hikFeature);
        return hikFeatureDao.update(hikFeature);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.hikFeatureDao.deleteById(id) > 0;
    }
}
