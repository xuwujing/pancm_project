package com.zans.portal.vo.radius;



import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class EndPointBatchReqVO {


    @NotNull
    private String ids;


    private Integer policy;


    private String authMark;

    private String username;

    private String traceId;

    private Integer debug;

}
