package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情推送
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/6
 */
@Data
public class PoManagerPushVO implements Serializable {


	/**
	 * 总条数
	 */
	private Integer total;

	/**
	 * 已分配条数
	 */
	private Integer assigned;

	/**
	 * 未分配条数
	 */
	private Integer unassigned;

	/**
	 * 责任人列表
	 */
	private List<String> dutyList;


}
