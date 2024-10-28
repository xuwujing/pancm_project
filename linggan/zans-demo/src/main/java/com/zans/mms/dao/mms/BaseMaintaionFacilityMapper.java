package com.zans.mms.dao.mms;

import com.zans.mms.model.BaseMaintaionFacility;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityTicketReqVO;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityQueryVO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BaseMaintaionFacilityMapper extends Mapper<BaseMaintaionFacility> {
    List<BaseMaintaionFacility> getList(BaseMaintaionFacilityQueryVO vo);

    List<BaseMaintaionFacility> getTicketList(BaseMaintaionFacilityTicketReqVO vo);
}
