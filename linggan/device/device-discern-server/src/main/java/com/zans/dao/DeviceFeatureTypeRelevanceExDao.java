package com.zans.dao;

import com.zans.model.DeviceFeatureTypeRelevanceEx;
import com.zans.vo.DeviceFeatureTypeRelevanceExVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备类型关联扩展表(DeviceFeatureTypeRelevanceEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:37:35
 */
@Mapper
public interface DeviceFeatureTypeRelevanceExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureTypeRelevanceExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureTypeRelevanceExVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureTypeRelevanceExVO findOne(DeviceFeatureTypeRelevanceExVO deviceFeatureTypeRelevanceExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureTypeRelevanceExVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureTypeRelevanceExVO> queryAll(DeviceFeatureTypeRelevanceExVO deviceFeatureTypeRelevanceExVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureTypeRelevanceEx 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureTypeRelevanceEx deviceFeatureTypeRelevanceEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureTypeRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureTypeRelevanceEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureTypeRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureTypeRelevanceEx> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureTypeRelevanceEx 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureTypeRelevanceEx deviceFeatureTypeRelevanceEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

