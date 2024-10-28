package com.zans.portal.dao;

import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.AssetBaselineVersion;
import com.zans.portal.vo.AssetBaselineVersionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 基线变更记录表(AssetBaselineVersion)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:10:22
 */
@Repository
public interface AssetBaselineVersionDao extends Mapper<AssetBaselineVersion> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetBaselineVersionVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 对象列表
     */
    AssetBaselineVersionVO findOne(AssetBaselineVersionVO assetBaselineVersionVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetBaselineVersionVO 实例对象
     * @return 对象列表
     */
    List<AssetBaselineVersionVO> queryAll(AssetBaselineVersionVO assetBaselineVersionVO);

    /**
     * 新增数据
     *
     * @param assetBaselineVersion 实例对象
     * @return 影响行数
     */
    int insert(AssetBaselineVersion assetBaselineVersion);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetBaselineVersion> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetBaselineVersion> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetBaselineVersion> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetBaselineVersion> entities);

    /**
     * 修改数据
     *
     * @param assetBaselineVersion 实例对象
     * @return 影响行数
     */
    int update(AssetBaselineVersion assetBaselineVersion);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);
}

