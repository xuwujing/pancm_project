package com.zans.dao;

import com.zans.model.DeviceFeatureModelRelevanceEx;
import com.zans.vo.DeviceFeatureModelRelevanceExVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备型号关联扩展表(DeviceFeatureModelRelevanceEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:22:16
 */
@Mapper
public interface DeviceFeatureModelRelevanceExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureModelRelevanceExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureModelRelevanceExVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureModelRelevanceExVO findOne(DeviceFeatureModelRelevanceExVO deviceFeatureModelRelevanceExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureModelRelevanceExVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureModelRelevanceExVO> queryAll(DeviceFeatureModelRelevanceExVO deviceFeatureModelRelevanceExVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureModelRelevanceEx 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureModelRelevanceEx deviceFeatureModelRelevanceEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureModelRelevanceEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelRelevanceEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureModelRelevanceEx> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureModelRelevanceEx 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureModelRelevanceEx deviceFeatureModelRelevanceEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

