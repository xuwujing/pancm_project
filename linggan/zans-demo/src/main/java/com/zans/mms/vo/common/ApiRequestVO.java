package com.zans.mms.vo.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiRequestVO implements Serializable {

    private static final long serialVersionUID = 10000000L;
    private static final int CODE_SUCCESS = 0 ;
    private static final int CODE_ERROR_UNKNOWN = 500 ;

    private Integer groupId;

    private Object data;




}
