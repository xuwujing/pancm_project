package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.mms.model.BaseMaintaionFacility;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityTicketReqVO;
import com.zans.mms.vo.basemaintaionfacility.BaseMaintaionFacilityQueryVO;

/**
 * interface BaseMaintaionFacilityservice
 *
 * @author
 */
public interface IBaseMaintaionFacilityService extends BaseService<BaseMaintaionFacility>{


    ApiResult getList(BaseMaintaionFacilityQueryVO vo);

    ApiResult getTicketList(BaseMaintaionFacilityTicketReqVO vo);


}
