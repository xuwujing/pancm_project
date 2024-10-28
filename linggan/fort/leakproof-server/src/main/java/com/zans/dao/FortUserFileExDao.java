package com.zans.dao;

import com.zans.model.FortUserFileEx;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户文件详细信息(FortUserFileEx)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-19 10:48:49
 */
@Mapper
public interface FortUserFileExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    FortUserFileEx queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param fortUserFileEx 实例对象
     * @return 对象列表
     */
    List<FortUserFileEx> queryAll(FortUserFileEx fortUserFileEx);

    /**
     * 新增数据
     *
     * @param fortUserFileEx 实例对象
     * @return 影响行数
     */
    int insert(FortUserFileEx fortUserFileEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     * @return 影响行数
     */
    int insertBatch(List<FortUserFileEx> list);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FortUserFileEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<FortUserFileEx> entities);

    /**
     * 修改数据
     *
     * @param fortUserFileEx 实例对象
     * @return 影响行数
     */
    int update(FortUserFileEx fortUserFileEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

