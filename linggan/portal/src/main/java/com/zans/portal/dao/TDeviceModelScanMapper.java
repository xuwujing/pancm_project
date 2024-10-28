package com.zans.portal.dao;

import com.zans.portal.model.TDeviceModelScan;
import com.zans.portal.vo.model.ModelScanSearchVO;
import com.zans.portal.vo.model.ModelScanRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TDeviceModelScanMapper extends Mapper<TDeviceModelScan> {

    List<ModelScanRespVO> findModelList(@Param("req") ModelScanSearchVO req);

    ModelScanRespVO getModelById(@Param("id") Integer id);
}