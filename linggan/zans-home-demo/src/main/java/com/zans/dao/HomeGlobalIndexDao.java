package com.zans.dao;

import com.zans.model.HomeGlobalIndex;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 全局配置指标关联表(HomeGlobalIndex)表数据库访问层
 *
 * @author beixing
 * @since 2021-10-22 11:40:45
 */
@Mapper
public interface HomeGlobalIndexDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HomeGlobalIndex queryById(Integer id);


    HomeGlobalIndex queryOne(HomeGlobalIndex homeGlobalIndex);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param homeGlobalIndex 实例对象
     * @return 对象列表
     */
    List<HomeGlobalIndex> queryAll(HomeGlobalIndex homeGlobalIndex);

    /**
     * 新增数据
     *
     * @param homeGlobalIndex 实例对象
     * @return 影响行数
     */
    int insert(HomeGlobalIndex homeGlobalIndex);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeGlobalIndex> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HomeGlobalIndex> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeGlobalIndex> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HomeGlobalIndex> entities);

    /**
     * 修改数据
     *
     * @param homeGlobalIndex 实例对象
     * @return 影响行数
     */
    int update(HomeGlobalIndex homeGlobalIndex);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

