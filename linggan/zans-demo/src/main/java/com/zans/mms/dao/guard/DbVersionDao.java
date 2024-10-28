package com.zans.mms.dao.guard;


import com.zans.mms.model.DbVersion;
import com.zans.mms.vo.DbVersionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author beixing
 * @Title: (DbVersion)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:32
 */
@Mapper
public interface DbVersionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbVersionVO queryById(Long id);


    DbVersionVO queryNewOne();


    /**
     * 通过实体查询一条数据
     *
     * @param dbVersionVO 实例对象
     * @return 对象列表
     */
    DbVersionVO findOne(DbVersionVO dbVersionVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param dbVersionVO 实例对象
     * @return 对象列表
     */
    List<DbVersionVO> queryAll(DbVersionVO dbVersionVO);

    /**
     * 新增数据
     *
     * @param dbVersion 实例对象
     * @return 影响行数
     */
    int insert(DbVersion dbVersion);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbVersion> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DbVersion> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DbVersion> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DbVersion> entities);

    /**
     * 修改数据
     *
     * @param dbVersion 实例对象
     * @return 影响行数
     */
    int update(DbVersion dbVersion);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

