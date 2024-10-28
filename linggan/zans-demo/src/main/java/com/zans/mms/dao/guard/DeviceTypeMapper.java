package com.zans.mms.dao.guard;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.DeviceType;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DeviceTypeMapper extends Mapper<DeviceType> {

	List<SelectVO> findDeviceTypeToSelect();

}
