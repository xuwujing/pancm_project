package com.zans.mms.dao.mms;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseFaultType;
import com.zans.mms.vo.base.BaseFaultTypeVO;
import com.zans.mms.vo.chart.CountUnit;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wang
 */
@Repository
public interface BaseFaultTypeMapper extends Mapper<BaseFaultType> {

    /**
     * 获取所有的设备对应的故障原因
     * @return
     */
    public List<BaseFaultTypeVO> listFaultTypeView();


     List<SelectVO> faultList();

    List<CountUnit> getDefaultFaultType(List<String> list);

}
