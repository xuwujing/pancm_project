package com.zans.mms.vo.asset.subset;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "AssetSubsetLineChartReqVO", description = "资产子集在线率折线图接收参数实体类")
@Data
public class AssetSubsetLineChartReqVO {

	/**
	 * 设备类型编号
	 */
	private String deviceType;

	/**
	 * 是否历史数据
	 */
	private Boolean isHistory;


	/**
	 * 资产子集id
	 */
	private Long subsetId;
}
