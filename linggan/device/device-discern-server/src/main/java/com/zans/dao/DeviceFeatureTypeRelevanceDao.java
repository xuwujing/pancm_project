package com.zans.dao;

import com.zans.model.DeviceFeatureTypeRelevance;
import com.zans.vo.DeviceFeatureTypeRelevanceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备类型关联表(DeviceFeatureTypeRelevance)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 15:33:56
 */
@Mapper
public interface DeviceFeatureTypeRelevanceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureTypeRelevanceVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureTypeRelevanceVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureTypeRelevanceVO findOne(DeviceFeatureTypeRelevanceVO deviceFeatureTypeRelevanceVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureTypeRelevanceVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureTypeRelevanceVO> queryAll(DeviceFeatureTypeRelevanceVO deviceFeatureTypeRelevanceVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureTypeRelevance 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureTypeRelevance deviceFeatureTypeRelevance);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureTypeRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureTypeRelevance> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureTypeRelevance> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureTypeRelevance> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureTypeRelevance 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureTypeRelevance deviceFeatureTypeRelevance);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

