package com.zans.mms.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:筛选项实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/5
 */
@Data
public class Screen implements Serializable {

	private Long id;

	private String type;

	private String items;

	private String itemsValues;

	private String creator;


	private Date createTime;

	private Date updateTime;
}
