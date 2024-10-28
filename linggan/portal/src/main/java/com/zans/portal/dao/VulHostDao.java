package com.zans.portal.dao;

import com.zans.portal.model.VulHost;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (VulHost)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-25 16:58:24
 */
@Mapper
public interface VulHostDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    VulHost queryById(Long id);


   VulHost queryOne(VulHost vulHost);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param vulHost 实例对象
     * @return 对象列表
     */
    List<VulHost> queryAll(VulHost vulHost);

    /**
     * 新增数据
     *
     * @param vulHost 实例对象
     * @return 影响行数
     */
    int insert(VulHost vulHost);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<VulHost> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<VulHost> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<VulHost> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<VulHost> entities);

    /**
     * 修改数据
     *
     * @param vulHost 实例对象
     * @return 影响行数
     */
    int update(VulHost vulHost);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int deleteByIpAddr(String hostIp);
}

