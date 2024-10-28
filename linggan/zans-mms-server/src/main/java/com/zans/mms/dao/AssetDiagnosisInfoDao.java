package com.zans.mms.dao;

import com.zans.mms.model.AssetDiagnosisInfo;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfo)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:53:24
 */
@Mapper
public interface AssetDiagnosisInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosisInfoVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosisInfoVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosisInfoVO findOne(AssetDiagnosisInfoVO assetDiagnosisInfoVO);

    int isExist(String assetCode);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosisInfoVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosisInfoVO> queryAll(AssetDiagnosisInfoVO assetDiagnosisInfoVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosisInfo 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosisInfo assetDiagnosisInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosisInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosisInfo> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosisInfo 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosisInfo assetDiagnosisInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

