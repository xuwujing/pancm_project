package com.zans.dao;

import com.zans.model.DeviceFeatureInfo;
import com.zans.vo.DeviceFeatureInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备统计表(DeviceFeatureInfo)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-30 14:15:30
 */
@Mapper
public interface DeviceFeatureInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureInfoVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureInfoVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureInfoVO findOne(DeviceFeatureInfoVO deviceFeatureInfoVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureInfoVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureInfoVO> queryAll(DeviceFeatureInfoVO deviceFeatureInfoVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureInfo 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureInfo deviceFeatureInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureInfo> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureInfo 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureInfo deviceFeatureInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

