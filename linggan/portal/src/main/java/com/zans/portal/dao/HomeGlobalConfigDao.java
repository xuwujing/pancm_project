package com.zans.portal.dao;

import com.zans.portal.model.HomeGlobalConfig;
import com.zans.portal.vo.HomeGlobalConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页全局配置表(HomeGlobalConfig)表数据库访问层
 *
 * @author beixing
 * @since 2021-10-21 10:37:02
 */
@Mapper
public interface HomeGlobalConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HomeGlobalConfig queryById(Integer id);

    List<HomeGlobalConfigVo> queryByGlobalId(@Param("globalId") Integer globalId);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param homeGlobalConfig 实例对象
     * @return 对象列表
     */
    List<HomeGlobalConfig> queryAll(HomeGlobalConfig homeGlobalConfig);

    /**
     * 新增数据
     *
     * @param homeGlobalConfig 实例对象
     * @return 影响行数
     */
    int insert(HomeGlobalConfig homeGlobalConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeGlobalConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HomeGlobalConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeGlobalConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HomeGlobalConfig> entities);

    /**
     * 修改数据
     *
     * @param homeGlobalConfig 实例对象
     * @return 影响行数
     */
    int update(HomeGlobalConfig homeGlobalConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

