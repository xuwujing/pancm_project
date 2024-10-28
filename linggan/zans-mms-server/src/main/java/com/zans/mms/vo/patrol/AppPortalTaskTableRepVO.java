package com.zans.mms.vo.patrol;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:小程序巡检表格
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/26
 */
@Data
public class AppPortalTaskTableRepVO implements Serializable {

	/**
	 * 完成率
	 */
	private BigDecimal rate;


	/**
	 * 故障计数
	 */
	private Integer breakDownCount;

	/**
	 * 片区及类型
	 */
	List<Map<String,Object>> data;

	/**
	 * 设备类型
	 */
	List<String> deviceType;


}
