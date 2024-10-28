package com.zans.mms.vo.ticket;

import com.zans.mms.model.BaseVfs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:选图片用
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/2
 */
@Data
public class TicketImgVO implements Serializable {

	/**
	 * 附件图片列表
	 */
	private List<BaseVfs> baseVfsList;

	/**
	 * 操作人名称
	 */
	private String opName;

	/**
	 * 操作时间
	 */
	private String opTime;

	/**
	 * 操作名称
	 */
	private String operation;

	/**
	 * 备注信息
	 */
	private String msg;
}
