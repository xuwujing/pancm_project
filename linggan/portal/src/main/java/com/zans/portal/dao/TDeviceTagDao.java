package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.TDeviceTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型用户标签表(TDeviceTag)表数据库访问层
 *
 * @author beixing
 * @since 2022-02-23 16:00:02
 */
@Mapper
public interface TDeviceTagDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TDeviceTag queryById(Integer id);


    List<SelectVO> findDeviceTypeToSelect();


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tDeviceTag 实例对象
     * @return 对象列表
     */
    List<TDeviceTag> queryAll(TDeviceTag tDeviceTag);

    /**
     * 新增数据
     *
     * @param tDeviceTag 实例对象
     * @return 影响行数
     */
    int insert(TDeviceTag tDeviceTag);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TDeviceTag> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TDeviceTag> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TDeviceTag> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TDeviceTag> entities);

    /**
     * 修改数据
     *
     * @param tDeviceTag 实例对象
     * @return 影响行数
     */
    int update(TDeviceTag tDeviceTag);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

