package com.zans.portal.dao;

import com.zans.portal.model.SysCustomField;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 自定义表字段(SysCustomField)表数据库访问层
 *
 * @author beixing
 * @since 2021-12-02 12:04:01
 */
@Mapper
public interface SysCustomFieldDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysCustomField queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysCustomField 实例对象
     * @return 对象列表
     */
    List<SysCustomField> queryAll(SysCustomField sysCustomField);

    /**
     * 新增数据
     *
     * @param sysCustomField 实例对象
     * @return 影响行数
     */
    int insert(SysCustomField sysCustomField);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysCustomField> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysCustomField> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysCustomField> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysCustomField> entities);

    /**
     * 修改数据
     *
     * @param sysCustomField 实例对象
     * @return 影响行数
     */
    int update(SysCustomField sysCustomField);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
