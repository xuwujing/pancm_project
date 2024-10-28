package com.zans.portal.dao;

import com.zans.portal.model.AssetBaselineArea;
import com.zans.portal.vo.AssetBaselineAreaPageVO;
import com.zans.portal.vo.AssetBaselineAreaVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetBaselineArea)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-09-14 17:04:05
 */
@Repository
public interface AssetBaselineAreaDao extends Mapper<AssetBaselineArea> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetBaselineAreaVO queryById(Long id);

    AssetBaselineAreaVO queryByIp(String ip);


    AssetBaselineAreaVO queryByAreaName(String areaName);




    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetBaselineAreaVO 实例对象
     * @return 对象列表
     */
    List<AssetBaselineAreaVO> queryAll(AssetBaselineAreaPageVO assetBaselineAreaVO);

    /**
     * 新增数据
     *
     * @param assetBaselineArea 实例对象
     * @return 影响行数
     */
    int insert(AssetBaselineArea assetBaselineArea);


    /**
     * 修改数据
     *
     * @param assetBaselineArea 实例对象
     * @return 影响行数
     */
    int update(AssetBaselineArea assetBaselineArea);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    AssetBaselineArea getByName(@Param("areaName") String areaName);


    AssetBaselineArea getOnlyLevel();
}

