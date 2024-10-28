package com.zans.portal.vo.chart;

import lombok.Data;

/**
 * 两次Group的统计单元
 * @author xv
 * @since 2020/4/2 16:58
 */
@Data
public class TwoDimensionGroupUnit {

    private Integer dimOne;

    private Integer dimTwo;

    private Integer val;
}
