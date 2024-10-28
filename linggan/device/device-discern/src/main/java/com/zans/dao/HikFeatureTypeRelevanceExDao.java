package com.zans.dao;

import com.zans.model.HikFeatureTypeRelevanceEx;
import com.zans.vo.HikFeatureTypeRelevanceExVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备类型关联扩展表(HikFeatureTypeRelevanceEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-15 16:27:53
 */
@Mapper
public interface HikFeatureTypeRelevanceExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureTypeRelevanceExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param hikFeatureTypeRelevanceExVO 实例对象
     * @return 对象列表
     */
    HikFeatureTypeRelevanceExVO findOne(HikFeatureTypeRelevanceExVO hikFeatureTypeRelevanceExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureTypeRelevanceExVO 实例对象
     * @return 对象列表
     */
    List<HikFeatureTypeRelevanceExVO> queryAll(HikFeatureTypeRelevanceExVO hikFeatureTypeRelevanceExVO);

    /**
     * 新增数据
     *
     * @param hikFeatureTypeRelevanceEx 实例对象
     * @return 影响行数
     */
    int insert(HikFeatureTypeRelevanceEx hikFeatureTypeRelevanceEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureTypeRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HikFeatureTypeRelevanceEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeatureTypeRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HikFeatureTypeRelevanceEx> entities);

    /**
     * 修改数据
     *
     * @param hikFeatureTypeRelevanceEx 实例对象
     * @return 影响行数
     */
    int update(HikFeatureTypeRelevanceEx hikFeatureTypeRelevanceEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

