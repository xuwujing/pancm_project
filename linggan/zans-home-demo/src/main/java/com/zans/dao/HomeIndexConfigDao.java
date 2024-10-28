package com.zans.dao;

import com.zans.model.HomeIndexConfig;
import com.zans.vo.HomeIndexConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页指标表(HomeIndexConfig)表数据库访问层
 *
 * @author beixing
 * @since 2021-10-22 15:24:06
 */
@Mapper
public interface HomeIndexConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HomeIndexConfigVO queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param homeIndexConfig 实例对象
     * @return 对象列表
     */
    List<HomeIndexConfig> queryAll(HomeIndexConfig homeIndexConfig);

    /**
     * 新增数据
     *
     * @param homeIndexConfig 实例对象
     * @return 影响行数
     */
    int insert(HomeIndexConfig homeIndexConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeIndexConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HomeIndexConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HomeIndexConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HomeIndexConfig> entities);

    /**
     * 修改数据
     *
     * @param homeIndexConfig 实例对象
     * @return 影响行数
     */
    int update(HomeIndexConfig homeIndexConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

