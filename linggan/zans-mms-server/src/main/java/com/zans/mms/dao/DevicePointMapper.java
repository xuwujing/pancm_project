package com.zans.mms.dao;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.vo.asset.ExcelAssetVO;
import com.zans.mms.vo.devicepoint.*;
import com.zans.mms.vo.devicepoint.map.DevicePointForMapResVO;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.devicepoint.map.TicketForMapResVO;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddByConditionReqVO;
import com.zans.mms.vo.po.PoManagerResVO;
import com.zans.mms.vo.ticket.TicketDevicePointMapQueryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface DevicePointMapper extends Mapper<DevicePoint> {

	List<DevicePointResVO> getList(DevicePointQueryVO vo);

	DevicePointDetailResVO getViewById(Long id);

	int deleteByUniqueId(Long id);

	Integer getIdByUniqueId(String pointId);

	void insertDevicePoint(ExcelDevicePointVO devicePoint);

	Integer getByCode(String pointCode);

	void updateByPointCode(ExcelDevicePointVO devicePoint);

	void deleteByPointCode(String pointCode);

	Long getIdByCode(String pointCode);

	void updateByPointCodeChange(ExcelDevicePointVO vo);

	List<Long> getPointIdByCondition(PointSubsetDetailAddByConditionReqVO reqVO);

	void updateDevicePoint(DevicePoint devicePoint);

	int  saveDevicePoint(DevicePointAddReqVO devicePointAddReqVO);

	/**
	 * @Author beiming
	 * @Description  点位地图
	 * @Date  4/27/21
	 * @Param
	 * @return
	 **/
	List<DevicePointForMapResVO> getListForMap(DevicePointMapQueryVO vo);

	/**
	 * 通过orgId获取点位数据
	 * @param orgId
	 * @return
	 */
	List<DevicePointExportVO> getListByOrgId(String orgId);

	/**
	 * 导出数据
	 * @param devicePointQueryVO
	 * @return
	 */
	List<DevicePointExportVO> getExportList(DevicePointExportQueryVO devicePointQueryVO);

	/**
	 * 根据巡检子集id查询数据
	 * @param subsetId
	 * @return
	 */
	List<DevicePointExportVO> getExportListBySubsetId(Long subsetId);

	List<Map<String,Object>> getTrafficSignalList(PoManagerResVO vo);

	List<TicketForMapResVO> getListForTicketMap(TicketDevicePointMapQueryVO vo);

	String selectPointName(@Param("id") Long id);


	Long getidByNameAndDeviceType(@Param("pointName") String pointName, @Param("deviceType") String deviceType);

	List<Map<Long, String>> getAllPoint();

	List<String> getBuildCompany();

	void synchronousProjectId();

	void synchronousBuildCompany();

	void setDefaultProjectId();

	void synchronousItemClassification();

	DevicePoint getOne(@Param("id") Long id);

	void backupTable(@Param("backupTableName") String backupTableName);

	void synchronousData(@Param("backupTableName") String backupTableName);

	ExcelDevicePointVO getOneByCode(@Param("pointCode") String pointCodeChange);

	List<DevicePointResVO> getImportList(DevicePointQueryVO vo);

	void updateImportDevicePoint(DevicePoint devicePoint);

	List<ExcelDevicePointVO> getByImportId(Long importId);

	void insertOne(ExcelDevicePointVO excelDevicePointVO);

	void updateExcelDevicePoint(ExcelDevicePointVO excelDevicePointVO);


}
