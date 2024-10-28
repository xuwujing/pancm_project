package com.zans.mms.dao;

import com.zans.mms.model.AssetDiagnosisFlagInfoHis;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisFlagInfoHisVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (AssetDiagnosisFlagInfoHis)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-24 16:02:16
 */
@Mapper
public interface AssetDiagnosisFlagInfoHisDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssetDiagnosisFlagInfoHisVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param assetDiagnosisFlagInfoHisVO 实例对象
     * @return 对象列表
     */
    AssetDiagnosisFlagInfoHisVO findOne(AssetDiagnosisFlagInfoHisVO assetDiagnosisFlagInfoHisVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param assetDiagnosisFlagInfoHisVO 实例对象
     * @return 对象列表
     */
    List<AssetDiagnosisFlagInfoHisVO> queryAll(AssetDiagnosisFlagInfoHisVO assetDiagnosisFlagInfoHisVO);

    /**
     * 新增数据
     *
     * @param assetDiagnosisFlagInfoHis 实例对象
     * @return 影响行数
     */
    int insert(AssetDiagnosisFlagInfoHis assetDiagnosisFlagInfoHis);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisFlagInfoHis> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssetDiagnosisFlagInfoHis> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssetDiagnosisFlagInfoHis> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssetDiagnosisFlagInfoHis> entities);

    /**
     * 修改数据
     *
     * @param assetDiagnosisFlagInfoHis 实例对象
     * @return 影响行数
     */
    int update(AssetDiagnosisFlagInfoHis assetDiagnosisFlagInfoHis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

