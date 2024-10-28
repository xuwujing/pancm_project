package com.zans.mms.vo.basemaintaionfacility;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "BaseMaintaionFacilityQueryVO", description = "")
@Data
public class BaseMaintaionFacilityTicketReqVO extends BasePage implements Serializable {


    private String deviceCategory;
    private String deviceName;
    private String orgId;
    private String deviceCode;

    private Long ticketId;
    private Integer type;

}
