package com.zans.mms.dao.mms;

import com.zans.mms.model.DevicePoint;
import com.zans.mms.vo.devicepoint.*;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddByConditionReqVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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
}
