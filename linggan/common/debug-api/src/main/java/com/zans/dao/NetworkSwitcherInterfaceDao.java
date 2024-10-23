package com.zans.dao;

import com.zans.model.NetworkSwitcherInterface;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (NetworkSwitcherInterface)表数据库访问层
 *
 * @author beixing
 * @since 2022-03-15 18:01:55
 */
@Mapper
public interface NetworkSwitcherInterfaceDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    NetworkSwitcherInterface queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param networkSwitcherInterface 实例对象
     * @return 对象列表
     */
    List<NetworkSwitcherInterface> queryAll(NetworkSwitcherInterface networkSwitcherInterface);

    /**
     *
     * @param swIp
     * @return
     */
    List<NetworkSwitcherInterface> findItfLimitList(String swIp);

    /**
     * 新增数据
     *
     * @param networkSwitcherInterface 实例对象
     * @return 影响行数
     */
    int insert(NetworkSwitcherInterface networkSwitcherInterface);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<NetworkSwitcherInterface> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<NetworkSwitcherInterface> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<NetworkSwitcherInterface> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<NetworkSwitcherInterface> entities);

    /**
     * 修改数据
     *
     * @param networkSwitcherInterface 实例对象
     * @return 影响行数
     */
    int update(NetworkSwitcherInterface networkSwitcherInterface);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

