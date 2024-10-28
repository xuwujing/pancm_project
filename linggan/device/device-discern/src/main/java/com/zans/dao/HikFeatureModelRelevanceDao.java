package com.zans.dao;

import com.zans.model.HikFeatureModelRelevance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备型号关联表(HikFeatureModelRelevance)表数据库访问层
 *
 * @author beixing
 * @since 2021-07-15 14:33:07
 */
@Mapper
public interface HikFeatureModelRelevanceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureModelRelevance queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureModelRelevance 实例对象
     * @return 对象列表
     */
    List<HikFeatureModelRelevance> queryAll(HikFeatureModelRelevance hikFeatureModelRelevance);

    /**
     * 新增数据
     *
     * @param hikFeatureModelRelevance 实例对象
     * @return 影响行数
     */
    int insert(HikFeatureModelRelevance hikFeatureModelRelevance);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureModelRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HikFeatureModelRelevance> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureModelRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HikFeatureModelRelevance> entities);

    /**
     * 修改数据
     *
     * @param hikFeatureModelRelevance 实例对象
     * @return 影响行数
     */
    int update(HikFeatureModelRelevance hikFeatureModelRelevance);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
