package com.zans.mms.util;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/4/25
 */
public enum  BpmsActivityTypeEnum {
	START_EVENT("startEvent", "开始事件"),
	END_EVENT("endEvent", "结束事件"),
	USER_TASK("userTask", "用户任务"),
	EXCLUSIVE_GATEWAY("exclusiveGateway", "排他网关"),
	PARALLEL_GATEWAY("parallelGateway", "并行网关"),
	INCLUSIVE_GATEWAY("inclusiveGateway", "包含网关");
	private String type;
	private String name;
	private BpmsActivityTypeEnum(String type, String name) {
		this.type = type;
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
