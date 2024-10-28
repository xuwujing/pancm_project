package com.zans.portal.vo.segment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: portal
 * @Description:ip地址池显示详情
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Data
public class NetworkIpSegmentViewVO implements Serializable {

	/**
	 * ip地址组
	 */
	private String ipGroup;

	/**
	 * ip地址段
	 */
	private String ipSegment;

	/**
	 * 未使用ip数
	 */
	private Integer notUsedIpNum;


	/**
	 * 在线ip数
	 */
	private Integer onlineIpNum;

	/**
	 * 离线ip数
	 */
	private Integer offlineIpNum;

	/**
	 * 已分配ip数量
	 */
	private Integer assignedIpNum;

	/**
	 * 地址信息
	 */
	private List<NetworkIPSegmentVO> networkIPSegmentVOList;
}
