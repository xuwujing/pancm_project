package com.zans.mms.vo.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/27
 */
@Data
public class PoManagerPointReqVO implements Serializable {
	private String deviceType;

	private String pointName;
}
