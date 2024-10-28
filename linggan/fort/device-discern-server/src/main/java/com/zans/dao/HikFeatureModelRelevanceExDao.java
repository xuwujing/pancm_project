package com.zans.dao;

import com.zans.model.HikFeatureModelRelevanceEx;
import com.zans.vo.HikFeatureModelRelevanceExVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备型号关联扩展表(HikFeatureModelRelevanceEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:29
 */
@Mapper
public interface HikFeatureModelRelevanceExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureModelRelevanceExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param hikFeatureModelRelevanceExVO 实例对象
     * @return 对象列表
     */
    HikFeatureModelRelevanceExVO findOne(HikFeatureModelRelevanceExVO hikFeatureModelRelevanceExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureModelRelevanceExVO 实例对象
     * @return 对象列表
     */
    List<HikFeatureModelRelevanceExVO> queryAll(HikFeatureModelRelevanceExVO hikFeatureModelRelevanceExVO);

    /**
     * 新增数据
     *
     * @param hikFeatureModelRelevanceEx 实例对象
     * @return 影响行数
     */
    int insert(HikFeatureModelRelevanceEx hikFeatureModelRelevanceEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureModelRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HikFeatureModelRelevanceEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureModelRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HikFeatureModelRelevanceEx> entities);

    /**
     * 修改数据
     *
     * @param hikFeatureModelRelevanceEx 实例对象
     * @return 影响行数
     */
    int update(HikFeatureModelRelevanceEx hikFeatureModelRelevanceEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

