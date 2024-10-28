package com.zans.portal.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: portal
 * @Description:网络ip地址池
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/23
 */
@Data
public class NetworkIpSegment implements Serializable {

	/**
	 * 主键 id 自增
	 */
	private Long id;


	/**
	 * ip段起始
	 */
	private String ipBegin;

	/**
	 * ip段终止
	 */
	private String ipEnd;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * c级ip列表
	 */
	List<String> cLevelIp;

}
