package com.zans.mms.vo.po;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情重复标记参数接受实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/12
 */
@Data
public class PoManagerRepeatMarkVO implements Serializable {

	/**
	 * 重复的id列表
	 */
	private List<Long> ids;

	/**
	 * 修复状态
	 */
	Integer repairStatus;

	/**
	 * 确认人描述
	 */
	private String confirmerDescription;


	/**
	 * 结案人描述
	 */
	private String closingPersonDescription;


	/**
	 * 创建人
	 */
	private String creator;

}
