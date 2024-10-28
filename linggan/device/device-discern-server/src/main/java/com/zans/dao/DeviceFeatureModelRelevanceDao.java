package com.zans.dao;

import com.zans.model.DeviceFeatureModelRelevance;
import com.zans.vo.DeviceFeatureModelRelevanceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备型号关联表(DeviceFeatureModelRelevance)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:14:55
 */
@Mapper
public interface DeviceFeatureModelRelevanceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureModelRelevanceVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureModelRelevanceVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureModelRelevanceVO findOne(DeviceFeatureModelRelevanceVO deviceFeatureModelRelevanceVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureModelRelevanceVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureModelRelevanceVO> queryAll(DeviceFeatureModelRelevanceVO deviceFeatureModelRelevanceVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureModelRelevance 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureModelRelevance deviceFeatureModelRelevance);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureModelRelevance> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureModelRelevance> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureModelRelevance 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureModelRelevance deviceFeatureModelRelevance);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

