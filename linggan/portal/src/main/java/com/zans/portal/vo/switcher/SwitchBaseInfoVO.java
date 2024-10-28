package com.zans.portal.vo.switcher;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: portal
 * @Description:交换机基础信息实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/24
 */
@Data
public class SwitchBaseInfoVO implements Serializable {

	private String label;

	private String id;

}
