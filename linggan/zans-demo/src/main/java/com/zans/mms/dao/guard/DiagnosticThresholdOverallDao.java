package com.zans.mms.dao.guard;

import com.zans.mms.model.DiagnosticThresholdOverall;
import com.zans.mms.vo.asset.diagnosis.DiagnosticThresholdOverallVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: (DiagnosticThresholdOverall)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-26 16:51:52
 */
@Mapper
public interface DiagnosticThresholdOverallDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DiagnosticThresholdOverallVO queryById(Long id);


    /**
     * 通过实体查询一条数据
     *
     * @param diagnosticThresholdOverallVO 实例对象
     * @return 对象列表
     */
    DiagnosticThresholdOverallVO findOne(DiagnosticThresholdOverallVO diagnosticThresholdOverallVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param diagnosticThresholdOverallVO 实例对象
     * @return 对象列表
     */
    List<DiagnosticThresholdOverallVO> queryAll(DiagnosticThresholdOverallVO diagnosticThresholdOverallVO);

    /**
     * 新增数据
     *
     * @param diagnosticThresholdOverall 实例对象
     * @return 影响行数
     */
    int insert(DiagnosticThresholdOverall diagnosticThresholdOverall);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DiagnosticThresholdOverall> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DiagnosticThresholdOverall> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DiagnosticThresholdOverall> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DiagnosticThresholdOverall> entities);

    /**
     * 修改数据
     *
     * @param diagnosticThresholdOverall 实例对象
     * @return 影响行数
     */
    int update(DiagnosticThresholdOverall diagnosticThresholdOverall);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

