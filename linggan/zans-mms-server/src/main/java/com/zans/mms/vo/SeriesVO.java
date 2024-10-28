package com.zans.mms.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * echart数据封装实体
 * @author qitian
 */
@ApiModel(value = "seriesVO", description = "echart数据封装实体")
@Data
public class SeriesVO {

	private String name;

	private String type;

	private String stack;

	private List<String> data;
}
