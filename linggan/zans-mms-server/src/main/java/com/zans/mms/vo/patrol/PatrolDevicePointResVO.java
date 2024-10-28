package com.zans.mms.vo.patrol;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zans.mms.config.PatrolConstants;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;

/**
 * @author beiso
 * @date 2021/3/10 9:41
 */
@Data
public class PatrolDevicePointResVO implements Serializable {

    private String pointId;

    private String pointCode;

    private String pointName;

    private String areaId;

    @JsonIgnore
    private String areaName;

    private String deviceTypeId;

    @JsonIgnore
    private String deviceTypeName;

    private String distance;

    private String longitude;

    private String latitude;

    private Integer checkStatus;

    private String startDate;

    private String endDate;

    private Integer remainingDays;

    static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void processRemainingDays() {
        if (remainingDays == null && checkStatus != null && endDate != null) {
            LocalDate endLocalDate = LocalDate.parse(endDate, DATE_FORMATTER);
            Period period = Period.between(LocalDate.now(), endLocalDate);
            int days = period.getDays() + 1;
            this.remainingDays = days >= PatrolConstants.REMAINING_DAY_TWOPLUS ? PatrolConstants.REMAINING_DAY_TWOPLUS : days;
        }
    }

    public static void main(String[] args) {
        Period period = Period.between(LocalDate.now(), LocalDateTime.now().plusHours(8).toLocalDate());
        System.out.println(period.getDays());
    }


    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
        this.processRemainingDays();
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.processRemainingDays();
    }


}
