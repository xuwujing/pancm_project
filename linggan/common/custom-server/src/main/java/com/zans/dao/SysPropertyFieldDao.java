package com.zans.dao;

import com.zans.model.SysPropertyField;
import com.zans.vo.SelectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字段属性表(SysPropertyField)表数据库访问层
 *
 * @author beixing
 * @since 2022-04-19 15:10:26
 */
@Mapper
public interface SysPropertyFieldDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysPropertyField queryById(Integer id);


    List<SelectVO> init();



    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysPropertyField 实例对象
     * @return 对象列表
     */
    List<SysPropertyField> queryAll(SysPropertyField sysPropertyField);

    /**
     * 新增数据
     *
     * @param sysPropertyField 实例对象
     * @return 影响行数
     */
    int insert(SysPropertyField sysPropertyField);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPropertyField> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysPropertyField> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPropertyField> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysPropertyField> entities);

    /**
     * 修改数据
     *
     * @param sysPropertyField 实例对象
     * @return 影响行数
     */
    int update(SysPropertyField sysPropertyField);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

