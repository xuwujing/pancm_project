package com.zans.dao;

import com.zans.model.FortUserFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (FortUserFile)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-17 17:45:12
 */
@Mapper
public interface FortUserFileDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    FortUserFile queryById(Long id);

    FortUserFile queryByUser(String userName);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param fortUserFile 实例对象
     * @return 对象列表
     */
    List<FortUserFile> queryAll(FortUserFile fortUserFile);

    /**
     * 新增数据
     *
     * @param fortUserFile 实例对象
     * @return 影响行数
     */
    int insert(FortUserFile fortUserFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FortUserFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<FortUserFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FortUserFile> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<FortUserFile> entities);

    /**
     * 修改数据
     *
     * @param fortUserFile 实例对象
     * @return 影响行数
     */
    int update(FortUserFile fortUserFile);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

