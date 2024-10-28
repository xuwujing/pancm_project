package com.zans.mms.vo.push;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:掉线实体接收
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/10
 */
@Data
public class DropLine implements Serializable {

	/**
	 * ip地址
	 */
	private String ip;


	/**
	 * 点位名称
	 */
	private String pointName;


	/**
	 * 掉线时间
	 */
	private String dropLineTime;



	/**
	 * 掉线类型 掉光/掉电
	 */
	private String dropLineType;


	/**
	 * 项目名称
	 */
	private String projectName;


	/**
	 * 运营商
	 */
	private String operator;


	/**
	 * 推送类型  0：掉线 1：恢复
	 */
	private Integer sendType;


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
