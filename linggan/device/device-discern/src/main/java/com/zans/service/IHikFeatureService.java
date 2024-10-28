package com.zans.service;

import com.zans.vo.ApiResult;
import com.zans.vo.HikFeatureVO;


/**
 * @author beixing
 * @Title: (HikFeature)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-14 11:04:04
 */
public interface IHikFeatureService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureVO queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureVO 实例对象
     * @return 对象列表
     */
    ApiResult list(HikFeatureVO hikFeatureVO);


    /**
     * 新增数据
     *
     * @param hikFeatureVO 实例对象
     * @return 实例对象
     */
    int insert(HikFeatureVO hikFeatureVO);

    /**
     * 修改数据
     *
     * @param hikFeatureVO 实例对象
     * @return 实例对象
     */
    int update(HikFeatureVO hikFeatureVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
