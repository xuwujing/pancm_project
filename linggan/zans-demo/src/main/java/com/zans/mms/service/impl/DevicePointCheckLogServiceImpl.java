package com.zans.mms.service.impl;


import com.zans.base.exception.BusinessException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.mms.dao.mms.DevicePointCheckLogMapper;
import com.zans.mms.dao.mms.DevicePointMapper;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.DevicePointCheckLog;
import com.zans.mms.service.IDevicePointCheckLogService;
import com.zans.mms.vo.devicepoint.DevicePointDetailResVO;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
* @Title: DevicePointCheckLogServiceImpl
* @Description: 点位表校正日志ServiceImpl
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/12/21
*/
@Slf4j
@Service("devicePointCheckLogService")
public class DevicePointCheckLogServiceImpl extends BaseServiceImpl<DevicePointCheckLog> implements IDevicePointCheckLogService {


	@Autowired
	private DevicePointCheckLogMapper devicePointCheckLogMapper;
	@Autowired
    DevicePointMapper devicePointMapper;

    @Resource
    public void setDevicePointCheckLogMapper(DevicePointCheckLogMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.devicePointCheckLogMapper = baseMapper;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean checkPoint(DevicePointCheckReqVO reqVO) {
        DevicePointDetailResVO devicePoint = devicePointMapper.getViewById(reqVO.getPointId());
        if (null == devicePoint) {
            throw new BusinessException("不存在此点位#"+reqVO.getPointId());
        }
        reqVO.setPrevLatitude(devicePoint.getLatitude());
        reqVO.setPrevLongitude(devicePoint.getLongitude());
        int i = devicePointCheckLogMapper.insertOne(reqVO);
        DevicePoint updateDevicePoint = new DevicePoint();
        updateDevicePoint.setId(reqVO.getPointId());
        updateDevicePoint.setLatitude(new BigDecimal(reqVO.getLatitude()));
        updateDevicePoint.setLongitude(new BigDecimal(reqVO.getLongitude()));
        devicePointMapper.updateDevicePoint(updateDevicePoint);
        return i==1;
    }
}
