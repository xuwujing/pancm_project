package com.zans.mms.dao;

import com.zans.mms.model.AssetDiagnosisInfoEx;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoExVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



/**
 * @author beixing
 * @Title: (AssetDiagnosisInfoEx)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:42:21
 */
@Mapper
public interface AssetDiagnosisInfoExDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosisInfoExVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosisInfoExVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosisInfoExVO findOne(AssetDiagnosisInfoExVO assetDiagnosisInfoExVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosisInfoExVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosisInfoExVO> queryAll(AssetDiagnosisInfoExVO assetDiagnosisInfoExVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosisInfoEx 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosisInfoEx assetDiagnosisInfoEx);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfoEx> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosisInfoEx> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfoEx> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosisInfoEx> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosisInfoEx 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosisInfoEx assetDiagnosisInfoEx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

