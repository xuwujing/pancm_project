package com.zans.job.vo;

/**
 * @author xv
 * @since 2020/5/6 16:08
 */
public enum State {

    TRI_YEAR(1),

    TRI_MONTH(2),

    TRI_DAY(3),

    TRI_HOUR(4),

    TRI_SECOND(5),

    TRI_WEEK(6);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    State(Integer value) {
        this.value = value;
    }
}
