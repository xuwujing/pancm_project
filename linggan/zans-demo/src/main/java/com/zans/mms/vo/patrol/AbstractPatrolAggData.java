package com.zans.mms.vo.patrol;

import com.zans.mms.config.PatrolConstants;
import lombok.Data;

/**
* @Title: AbstractPatrolAggData
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @author beiso
* @Date  2021/3/11
**/

@Data
public abstract class AbstractPatrolAggData {

    private Integer total;
    private Integer completed;
    private Integer todayCompleted;

    public AbstractPatrolAggData() {
        this.total = 0;
        this.completed = 0;
        this.todayCompleted = 0;
    }


    protected Integer ifNullToZero(Integer value) {
        return value == null ? 0 : value;
    }

    public void setTotal(Integer total) {
        this.total = ifNullToZero(total);
    }

    public void setCompleted(Integer completed) {
        this.completed = ifNullToZero(completed);
    }

    public void setTodayCompleted(Integer todayCompleted) {
        this.todayCompleted = ifNullToZero(todayCompleted);
    }


    public void addTotal(Integer value) {
        this.total += ifNullToZero(value);
    }

    public void addCompleted(Integer value) {
        this.completed += ifNullToZero(value);
    }

    public void addTodayCompleted(Integer value) {
        this.todayCompleted += ifNullToZero(value);
    }

    protected Integer calcCompletedStatus() {
        if (this.getCompleted().intValue() == this.getTotal().intValue() || this.getTotal() == 0) {
            return PatrolConstants.STATUS_COMPLETED_DONE;
        } else if (this.getCompleted() / this.getTotal() >= PatrolConstants.VALUE_COMPLETED_NEARLY) {
            return PatrolConstants.STATUS_COMPLETED_NEARLY;
        } else {
            return PatrolConstants.STATUS_COMPLETED_FAR;
        }
    }
}
