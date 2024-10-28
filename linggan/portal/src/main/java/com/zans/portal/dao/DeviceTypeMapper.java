package com.zans.portal.dao;

import com.zans.base.vo.SelectVO;
import com.zans.portal.model.DeviceType;
import com.zans.portal.vo.device.DeviceTypeResVO;
import com.zans.portal.vo.device.DeviceTypeSearchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DeviceTypeMapper extends Mapper<DeviceType> {

    List<SelectVO> findDeviceTypeToSelect();

    List<SelectVO> findDeviceTypeHasTemplateToSelect();

    Integer findTypeByName(@Param("name") String name);

    List<DeviceTypeResVO> findDeviceTypeList(@Param("reqVo") DeviceTypeSearchVO reqVO);


    List<SelectVO> findMmsDeviceTypeToSelect();

}
