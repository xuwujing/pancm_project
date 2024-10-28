package com.zans.mms.vo.patrol;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.mms.config.PatrolConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
* @Title: PatrolAllDevicePointResVO
* @Description: app巡检全量数据VO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/15/21
*/
@Data
public class PatrolAllDevicePointResVO implements Serializable {
    /**
     *  SELECT
        p.point_id AS pId,
        p.device_type AS dType,
        x ( p.gis ) AS lon,
        y ( p.gis ) AS lat,
        date_format( r.end_time, '%Y-%m-%d' ) AS endDate
        r.check_status AS cStatus
     */
    private Integer id;

    @ApiModelProperty(value = "点位id")
    private String pId;

    @ApiModelProperty(value = "设备类型ID")
    private String dType;

    @ApiModelProperty(value = "点位纬度")
    private String longitude;

    @ApiModelProperty(value = "点位经度")
    private String latitude;

    @ApiModelProperty(value = "检查状态")
    private Integer cheStatus;

    @ApiModelProperty(value = "巡检结果")
    private String cheResult;

    @JSONField(serialize = false)
    private String endDate;

    @JSONField(serialize = false)
    private String checkTime;

    @ApiModelProperty(value = " 剩余天数 remainingDays")
    private Integer rDays;



    static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void processRemainingDays() {
        if (rDays == null && cheStatus != null && endDate != null) {
            LocalDate endLocalDate = LocalDate.parse(endDate, DATE_FORMATTER);
//            Period period = Period.between(LocalDate.now(), endLocalDate);
//            int days = period.getDays() + 1;
            Long days2 = endLocalDate.toEpochDay()-LocalDate.now().toEpochDay() + 1;
            int days= days2.intValue();
            this.rDays = days >= PatrolConstants.REMAINING_DAY_TWOPLUS ? PatrolConstants.REMAINING_DAY_TWOPLUS : days;
        }
    }

    public void setCheStatus(Integer cheStatus) {
        this.cheStatus = cheStatus;
        this.processRemainingDays();
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.processRemainingDays();
    }




}
