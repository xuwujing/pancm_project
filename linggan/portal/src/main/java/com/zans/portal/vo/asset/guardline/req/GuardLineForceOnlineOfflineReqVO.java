package com.zans.portal.vo.asset.guardline.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class GuardLineForceOnlineOfflineReqVO {
    private Integer policy;

    private List<Integer> assetId;

    private List<String> ipAddr;
}
