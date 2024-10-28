package com.zans.mms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PatrolAppCheckInfo implements Serializable {

    private Integer pointId;
    private String  pointCode;
    private String  pointName;
    private String  deviceType;
    private String  deviceName;

    private Integer checkResult;
    private Integer checkStatus;
    private String checkDate;
    private String checkSite;
    private String checkUser;
    private String checkNickName;
    private String checkRemark;
    private String adjunctUuid;
    private String prevCheckDate;
    private Integer prevCheckResult;
    private Integer prevCheckStatus;
    private String prevCheckUser;

    private List<BaseVfs> adjunctList;

    private static final long serialVersionUID = 1L;

}
