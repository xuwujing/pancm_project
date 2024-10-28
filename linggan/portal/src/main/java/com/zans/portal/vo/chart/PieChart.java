package com.zans.portal.vo.chart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PieChart {

    private String name;

    private Integer type;

    private List<CountUnit> data;
}
