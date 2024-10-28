package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.Speed;
import com.zans.mms.vo.devicepoint.*;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.po.PoManagerResVO;

import java.util.List;

/**
 * interface DevicePointservice
 *
 * @author
 */
public interface IDevicePointService extends BaseService<DevicePoint>{


    ApiResult getList(DevicePointQueryVO vo);

    DevicePointDetailResVO getViewById(Long id);

    Boolean existRelation(Long id);

    int deleteByUniqueId(Long id);

    Integer getIdByUniqueId(String pointId);

	List<ExcelDevicePointVO> batchAddDevicePoint(String newFileName, String originName, UserSession userSession);

    void updateDevicePoint(DevicePoint devicePoint);


    int insertDevicePoint(DevicePointAddReqVO devicePointAddReqVO);


    /**
    * @Author beiming
    * @Description  点位地图列表
    * @Date  4/27/21
    * @Param
    * @return
    **/
    ApiResult getMapList(DevicePointMapQueryVO vo);

    /**
     * 导出点位数据
     * @param devicePointQueryVO 点位查询实体
     * @param fileName 导出文件名称
     * @return
     */
	String export(DevicePointExportQueryVO devicePointQueryVO, String fileName);

	ApiResult getTrafficSignalList(PoManagerResVO vo);

	List<String> getBuildCompany();

	void dealUploadData( Integer operation,Speed speed, List<ExcelDevicePointVO> excelDevicePointVOList, String absoluteNewFilePath, UserSession userSession);

	void backupTable();

	ApiResult getImportList(DevicePointQueryVO vo);

	void updateImportDevicePoint(DevicePoint devicePoint);

	void importConfig(DevicePointQueryVO queryVO,UserSession userSession);
}
