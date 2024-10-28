package com.zans.portal.dao;

import com.zans.portal.model.NetworkSwitcherScanDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (NetworkSwitcherScanDay)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-11 12:28:45
 */
@Mapper
public interface NetworkSwitcherScanDayDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    NetworkSwitcherScanDay queryById(Long id);


    NetworkSwitcherScanDay queryLast();


    /**
     * 通过实体作为筛选条件查询
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 对象列表
     */
    List<NetworkSwitcherScanDay> queryAll(NetworkSwitcherScanDay networkSwitcherScanDay);

    /**
     * 新增数据
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 影响行数
     */
    int insert(NetworkSwitcherScanDay networkSwitcherScanDay);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<NetworkSwitcherScanDay> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<NetworkSwitcherScanDay> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<NetworkSwitcherScanDay> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<NetworkSwitcherScanDay> entities);

    /**
     * 修改数据
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 影响行数
     */
    int update(NetworkSwitcherScanDay networkSwitcherScanDay);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    NetworkSwitcherScanDay queryByIpAndDate(@Param("swIp") String swIp, @Param("yyyymmdd") String startTime1);
}

