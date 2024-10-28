package com.zans.portal.dao;

import com.zans.portal.model.AssetScanDay;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AssetScanDayMapper extends Mapper<AssetScanDay> {


    /**
     * 通过实体作为筛选条件查询
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 对象列表
     */
    List<AssetScanDay> queryAll(AssetScanDay networkSwitcherScanDay);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetScanDay queryById(Long id);

    AssetScanDay queryLast();

    /**
     * 新增数据
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 影响行数
     */
    @Override
    int insert(AssetScanDay networkSwitcherScanDay);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetScanDay> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetScanDay> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetScanDay> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetScanDay> entities);

    /**
     * 修改数据
     *
     * @param networkSwitcherScanDay 实例对象
     * @return 影响行数
     */
    int update(AssetScanDay networkSwitcherScanDay);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    AssetScanDay queryByIpAndDate(@Param("ipAddr") String ipAddr, @Param("yyyymmdd") String startTime1);
}
