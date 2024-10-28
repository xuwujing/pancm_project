package com.zans.mms.vo.asset.subset;

import com.zans.mms.model.Asset;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/23
 */
@Data
public class AssetExportVO  extends Asset implements Serializable {



	/**
	 * 点位编码
	 */
	private String pointCode;

	private String orgName;

	private String pointName;

	private String areaName;

}
