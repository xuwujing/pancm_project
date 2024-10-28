package com.zans.mms.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author beiso
 * @date 2021/3/11 17:49
 */
@Data
public class PatrolClockIn implements Serializable {

    @NonNull
    private Integer pointId;
    @NonNull
    private String checkUser;
    @NonNull
    private Integer checkResult;
    @NonNull
    private String checkRemark;
    @NonNull
    private String checkLongitude;
    @NonNull
    private String checkLatitude;
    @NonNull
    private String checkSite;
    @NonNull
    private String checkDate;
    @NonNull
    private String adjunctUuid;

    private String checkGis;
}
