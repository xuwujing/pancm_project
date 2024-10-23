package com.zans.dao;

import com.zans.model.SysConstantItem;
import com.zans.vo.SelectVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (SysConstantItem)表数据库访问层
 *
 * @author beixing
 * @since 2022-05-23 11:35:40
 */
@Mapper
public interface SysConstantItemDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysConstantItem queryById(Object id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysConstantItem 实例对象
     * @return 对象列表
     */
    List<SysConstantItem> queryAll(SysConstantItem sysConstantItem);


    List<SelectVO> queryByKey(String key);

    List<String> queryAllByKey();

    /**
     * 新增数据
     *
     * @param sysConstantItem 实例对象
     * @return 影响行数
     */
    int insert(SysConstantItem sysConstantItem);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysConstantItem> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysConstantItem> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysConstantItem> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysConstantItem> entities);

    /**
     * 修改数据
     *
     * @param sysConstantItem 实例对象
     * @return 影响行数
     */
    int update(SysConstantItem sysConstantItem);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}

