package com.zans.mms.dto.workflow;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:流程评论
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/28
 */
@Data
public class CommentDto implements Serializable {

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 组织id
	 */
	private String orgId;

	/**
	 * 组织名称
	 */
	private String orgName;

	/**
	 * 评论信息
	 */
	private String msg;

	/**
	 * 评论时间
	 */
	private Date createTime;

	/**
	 * 任务id  可以根据任务id 拓展查询任务信息做显示
	 */
	private String taskId;


}
