package com.zans.mms.dao.mms;

import com.zans.mms.model.LogOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (LogOperation)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-22 11:57:19
 */
@Mapper
public interface LogOperationDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LogOperation queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param logOperation 实例对象
     * @return 对象列表
     */
    List<LogOperation> queryAll(LogOperation logOperation);

    /**
     * 新增数据
     *
     * @param logOperation 实例对象
     * @return 影响行数
     */
    int insert(LogOperation logOperation);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogOperation> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LogOperation> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LogOperation> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<LogOperation> entities);

    /**
     * 修改数据
     *
     * @param logOperation 实例对象
     * @return 影响行数
     */
    int update(LogOperation logOperation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

