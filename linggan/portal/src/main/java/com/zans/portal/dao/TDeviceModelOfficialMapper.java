package com.zans.portal.dao;

import com.zans.portal.model.TDeviceModelOfficial;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import com.zans.portal.vo.device.ExcelUnknownDeviceVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TDeviceModelOfficialMapper extends Mapper<TDeviceModelOfficial> {

    List<DeviceResponseVO> findOfficialList(@Param("reqVo") DeviceSearchVO reqVo);

    List<ExcelUnknownDeviceVO> findUnknownDevicePage();

    int findOffcialCountByCode(@Param("modelCode") String modelCode, @Param("id") Integer id);

}