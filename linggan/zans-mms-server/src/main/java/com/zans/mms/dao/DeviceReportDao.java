package com.zans.mms.dao;

import com.zans.mms.model.DeviceReport;
import com.zans.mms.vo.devicereport.DeviceReportEditReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSaveReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchReqVO;
import com.zans.mms.vo.devicereport.DeviceReportSearchRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备上报(DeviceReport)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-08 17:21:37
 */
@Mapper
public interface DeviceReportDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceReport queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceReport 实例对象
     * @return 对象列表
     */
    List<DeviceReportSearchRespVO> queryAll(DeviceReportSearchReqVO deviceReport);

    List<DeviceReportSearchRespVO> queryAppAll(DeviceReportSearchReqVO deviceReport);

    /**
     * 新增数据
     *
     * @param deviceReport 实例对象
     * @return 影响行数
     */
    int insert(DeviceReportSaveReqVO deviceReport);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceReport> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceReport> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceReport> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceReport> entities);

    /**
     * 修改数据
     *
     * @param deviceReport 实例对象
     * @return 影响行数
     */
    int update(DeviceReportEditReqVO deviceReport);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

