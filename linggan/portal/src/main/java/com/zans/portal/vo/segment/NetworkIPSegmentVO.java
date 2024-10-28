package com.zans.portal.vo.segment;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: portal
 * @Description: 每个ip对应的信息
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Data
public class NetworkIPSegmentVO implements Serializable {

	/**
	 * 设备类型
	 */
	private Integer deviceType;


	/**
	 * 是否在线 0离线 1在线
	 */
	private Integer used;


	/**
	 * 设备的ip地址
	 */
	private String ip;

	/**
	 * 设备类型名称
	 */
	private String deviceTypeName;


}
