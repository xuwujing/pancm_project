package com.zans.dao;

import com.zans.model.DeviceCipherHis;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (DeviceCipherHis)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-23 16:15:55
 */
@Mapper
public interface DeviceCipherHisDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipherHis queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceCipherHis 实例对象
     * @return 对象列表
     */
    List<DeviceCipherHis> queryAll(DeviceCipherHis deviceCipherHis);

    /**
     * 新增数据
     *
     * @param deviceCipherHis 实例对象
     * @return 影响行数
     */
    int insert(DeviceCipherHis deviceCipherHis);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipherHis> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceCipherHis> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipherHis> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceCipherHis> entities);

    /**
     * 修改数据
     *
     * @param deviceCipherHis 实例对象
     * @return 影响行数
     */
    int update(DeviceCipherHis deviceCipherHis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

