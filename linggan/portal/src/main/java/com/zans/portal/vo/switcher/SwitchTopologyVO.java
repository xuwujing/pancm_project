package com.zans.portal.vo.switcher;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: portal
 * @Description:核心交换机信息实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/24
 */
@Data
public class SwitchTopologyVO implements Serializable {

	private String from;

	private String to;


}
