package com.zans.mms.vo.po;

import com.zans.mms.vo.SeriesVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情折线图实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
@Data
public class PoManagerLineChartVO implements Serializable {

	/**
	 * 横轴列表
	 */
	private List<String> categories;

	/**
	 * 折线图数据实体
	 */
	List<SeriesVO> seriesVOList;


	private Integer max;


}
