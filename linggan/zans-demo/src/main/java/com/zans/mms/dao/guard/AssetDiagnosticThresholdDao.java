package com.zans.mms.dao.guard;

import com.zans.mms.model.AssetDiagnosticThreshold;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosticThreshold)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-26 16:52:28
 */
@Mapper
public interface AssetDiagnosticThresholdDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosticThresholdVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosticThresholdVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosticThresholdVO findOne(AssetDiagnosticThresholdVO assetDiagnosticThresholdVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosticThresholdVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosticThresholdVO> queryAll(AssetDiagnosticThresholdVO assetDiagnosticThresholdVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosticThreshold 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosticThreshold assetDiagnosticThreshold);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosticThreshold> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosticThreshold> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosticThreshold> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosticThreshold> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosticThreshold 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosticThreshold assetDiagnosticThreshold);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    int deleteByIp(String ipAddress,String deviceId);

}

