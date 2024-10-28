package com.zans.portal.dao;

import com.zans.portal.model.SysVersionInfo;
import com.zans.portal.vo.version.SysVersionInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (SysVersionInfo)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:22
 */
@Mapper
public interface SysVersionInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysVersionInfoVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param sysVersionInfoVO 实例对象
     * @return 对象列表
     */
    SysVersionInfoVO findOne(SysVersionInfoVO sysVersionInfoVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysVersionInfoVO 实例对象
     * @return 对象列表
     */
    List<SysVersionInfoVO> queryAll(SysVersionInfoVO sysVersionInfoVO);

    /**
     * 新增数据
     *
     * @param sysVersionInfo 实例对象
     * @return 影响行数
     */
    int insert(SysVersionInfo sysVersionInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysVersionInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysVersionInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysVersionInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysVersionInfo> entities);

    /**
     * 修改数据
     *
     * @param sysVersionInfo 实例对象
     * @return 影响行数
     */
    int update(SysVersionInfo sysVersionInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

