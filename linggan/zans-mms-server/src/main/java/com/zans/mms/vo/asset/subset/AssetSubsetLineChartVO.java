package com.zans.mms.vo.asset.subset;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetSubsetLineChartVO", description = "资产子集在线率折线图返回参数实体类")
@Data
public class AssetSubsetLineChartVO {

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
	private String statsTime;
}
