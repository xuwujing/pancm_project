package com.zans.portal.dao;

import com.zans.portal.model.VulHostVuln;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 漏洞详情表(VulHostVuln)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-25 16:58:25
 */
@Mapper
public interface VulHostVulnDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    VulHostVuln queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param vulHostVuln 实例对象
     * @return 对象列表
     */
    List<VulHostVuln> queryAll(VulHostVuln vulHostVuln);

    /**
     * 新增数据
     *
     * @param vulHostVuln 实例对象
     * @return 影响行数
     */
    int insert(VulHostVuln vulHostVuln);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<VulHostVuln> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<VulHostVuln> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<VulHostVuln> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<VulHostVuln> entities);

    /**
     * 修改数据
     *
     * @param vulHostVuln 实例对象
     * @return 影响行数
     */
    int update(VulHostVuln vulHostVuln);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int deleteByIpAddr(String hostIp);
}

