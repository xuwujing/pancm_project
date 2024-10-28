package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.DevicePointSubset;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetQueryVO;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetResVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DevicePointSubsetMapper extends Mapper<DevicePointSubset> {
    List<DevicePointSubsetResVO> getList(DevicePointSubsetQueryVO vo);

    int deleteById(Long subsetId);

    List<SelectVO> getSelectList();

    String getIdBySubsetName(String subsetName);

	String getSubsetNameById(Long subsetId);
}