package com.zans.dao;

import com.zans.model.DeviceFeatureSample;
import com.zans.vo.DeviceFeatureSampleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备型号样本表(DeviceFeatureSample)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-20 17:23:17
 */
@Mapper
public interface DeviceFeatureSampleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureSampleVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureSampleVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureSampleVO findOne(DeviceFeatureSampleVO deviceFeatureSampleVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureSampleVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureSampleVO> queryAll(DeviceFeatureSampleVO deviceFeatureSampleVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureSample 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureSample deviceFeatureSample);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureSample> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureSample> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureSample> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureSample> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureSample 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureSample deviceFeatureSample);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

