package com.zans.dao;

import com.zans.model.HikFeatureRelevanceEx;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备型号关联扩展表(HikFeatureRelevanceEx)表数据库访问层
 *
 * @author beixing
 * @since 2021-07-15 10:43:18
 */
@Mapper
public interface HikFeatureRelevanceExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureRelevanceEx queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureRelevanceEx 实例对象
     * @return 对象列表
     */
    List<HikFeatureRelevanceEx> queryAll(HikFeatureRelevanceEx hikFeatureRelevanceEx);

    /**
     * 新增数据
     *
     * @param hikFeatureRelevanceEx 实例对象
     * @return 影响行数
     */
    int insert(HikFeatureRelevanceEx hikFeatureRelevanceEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HikFeatureRelevanceEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HikFeatureRelevanceEx> entities);

    /**
     * 修改数据
     *
     * @param hikFeatureRelevanceEx 实例对象
     * @return 影响行数
     */
    int update(HikFeatureRelevanceEx hikFeatureRelevanceEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

