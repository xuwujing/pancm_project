package com.zans.mms.dao;

import com.zans.mms.model.AssetDiagnosisFlagInfoEx;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisFlagInfoExVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisFlagInfoEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-24 16:01:59
 */
@Mapper
public interface AssetDiagnosisFlagInfoExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosisFlagInfoExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosisFlagInfoExVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosisFlagInfoExVO findOne(AssetDiagnosisFlagInfoExVO assetDiagnosisFlagInfoExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosisFlagInfoExVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosisFlagInfoExVO> queryAll(AssetDiagnosisFlagInfoExVO assetDiagnosisFlagInfoExVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosisFlagInfoEx 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosisFlagInfoEx assetDiagnosisFlagInfoEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisFlagInfoEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosisFlagInfoEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisFlagInfoEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosisFlagInfoEx> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosisFlagInfoEx 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosisFlagInfoEx assetDiagnosisFlagInfoEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int deleteByTraceId(String assetCode,String traceId);

}

