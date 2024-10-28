package com.zans.mms.vo.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/6
 */
@Data
public class PoManagerPushReqVO implements Serializable {

	private Long id;

	private List<String> repairPersonList;
}
