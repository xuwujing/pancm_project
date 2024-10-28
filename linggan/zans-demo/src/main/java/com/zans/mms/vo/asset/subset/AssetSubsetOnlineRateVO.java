package com.zans.mms.vo.asset.subset;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "AssetSubsetOnlineRateVO", description = "资产子集在线率显示实体类")
@Data
public class AssetSubsetOnlineRateVO {

	/**
	 * 子集名称
	 */
	private String subsetName;


	/**
	 * 在线率
	 */
	private String onlineRate;


	/**
	 * 统计时间
	 */
	@DateTimeFormat(pattern = "HH:mm")
	private Date statsTime;

	/**
	 * 子集id  用于传参与折线图联动
	 */
	private Long subsetId;
}
