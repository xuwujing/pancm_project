package com.zans.dao;

import com.zans.model.AsynTaskRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (AsynTaskRecord)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-22 11:31:54
 */
@Mapper
public interface AsynTaskRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AsynTaskRecord queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param asynTaskRecord 实例对象
     * @return 对象列表
     */
    List<AsynTaskRecord> queryAll(AsynTaskRecord asynTaskRecord);

    /**
     * 新增数据
     *
     * @param asynTaskRecord 实例对象
     * @return 影响行数
     */
    int insert(AsynTaskRecord asynTaskRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynTaskRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AsynTaskRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AsynTaskRecord> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AsynTaskRecord> entities);

    /**
     * 修改数据
     *
     * @param asynTaskRecord 实例对象
     * @return 影响行数
     */
    int update(AsynTaskRecord asynTaskRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

