package com.zans.portal.vo.asset.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: portal
 * @Description:修改经纬度实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/7
 */
@Data
public class AssetEditCoordinatesReqVO implements Serializable {

	private Long id;

	private String longitude;

	private String latitude;

	private String ipAddr;


}
