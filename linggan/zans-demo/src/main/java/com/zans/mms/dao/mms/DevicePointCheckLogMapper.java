package com.zans.mms.dao.mms;

import com.zans.mms.model.DevicePointCheckLog;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
* @Title: DevicePointCheckLogMapper
* @Description: 点位表校正日志Mapper
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/12/21
*/
@Repository
public interface DevicePointCheckLogMapper extends Mapper<DevicePointCheckLog> {


    /**
    * @Author beiming
    * @Description  新增一条记录
    * @Date  4/12/21
    * @Param DevicePointCheckReqVO
    * @return
    **/
    int insertOne(DevicePointCheckReqVO reqVO);
}
