package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseDeviceType;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BaseDeviceTypeMapper extends Mapper<BaseDeviceType> {
    List<SelectVO> findDeviceTypeToSelect();

    String getTypeIdByName(String name);
}