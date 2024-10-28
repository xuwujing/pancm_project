package com.zans.mms.dao;

import com.zans.mms.model.AssetDiagnosisInfoHis;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoHisVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfoHis)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 18:24:44
 */
@Mapper
public interface AssetDiagnosisInfoHisDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosisInfoHisVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosisInfoHisVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosisInfoHisVO findOne(AssetDiagnosisInfoHisVO assetDiagnosisInfoHisVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosisInfoHisVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosisInfoHisVO> queryAll(AssetDiagnosisInfoHisVO assetDiagnosisInfoHisVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosisInfoHis 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosisInfoHis assetDiagnosisInfoHis);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfoHis> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosisInfoHis> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfoHis> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosisInfoHis> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosisInfoHis 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosisInfoHis assetDiagnosisInfoHis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

