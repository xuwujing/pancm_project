package com.zans.mms.vo.patrol;

import com.zans.mms.config.PatrolConstants;
import lombok.Data;

import java.util.List;

/**
 * @author beiso
 * @Description: //TODO
 * @date 2021/3/10 18:26
 */
@Data
public class PatrolDevicePointReqVO {

    private Integer pointId;

    private String selfLongitude;

    private String selfLatitude;

    private String moveLongitude;

    private String moveLatitude;

    private String[] deviceTypes;
    /**
     * 辖区ids
     */
    private String[] areaIds;

    /**
     * 巡检剩余天数list
     */
    private List<Integer> remainingDayList;


    private Integer remainingDays;

    private Integer radius;

    private Integer containCompleted;

    private String orgId;

    /**
     * 上一次查询时间
     */
    private String lastTime;

    /**
     * 本次查询时间
     */
    private String nowTime;

    /**
     * 后台返回未打卡的 10条数据    limitFlag = 1
     */
    private Integer limitFlag;

    /**
     * 项目列表过滤
     */
    private List<Integer> itemList;

    public PatrolDevicePointReqVO() {
        this.remainingDays = PatrolConstants.REMAINING_DAY_TWOPLUS;
        this.radius = PatrolConstants.DEFAULT_APP_SEARCH_RADIUS;
        this.containCompleted = 0;
    }

}
