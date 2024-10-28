package com.zans.mms.vo.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description: 工单导入数据实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/23
 */
@Data
public class TicketImportVO implements Serializable {

	@ApiModelProperty(value = "辖区")
	@ExcelProperty(value = "辖区", index = 0, validate = {"not_empty"})
	private String areaName;

	@ApiModelProperty(value = "工单类型")
	@ExcelProperty(value = "工单类型", index = 1)
	private String ticketType;

	@ApiModelProperty(value = "设备类型")
	@ExcelProperty(value = "设备类型", index = 2, validate = {"not_empty"})
	private String deviceType;

	@ApiModelProperty(value = "故障类型")
	@ExcelProperty(value = "故障类型", index = 3)
	private String issueType;

	@ApiModelProperty(value = "故障来源")
	@ExcelProperty(value = "故障来源", index = 4, validate = {"not_empty"})
	private String issueSource;

	@ApiModelProperty(value = "故障时间")
	@ExcelProperty(value = "故障时间", index = 5)
	private String occurredTime;

	@ApiModelProperty(value = "联系人")
	@ExcelProperty(value = "联系人", index = 6)
	private String applyContact;

	@ApiModelProperty(value = "联系人电话")
	@ExcelProperty(value = "联系人电话", index = 7)
	private String applyPhone;


	@ApiModelProperty(value = "问题描述")
	@ExcelProperty(value = "问题描述", index = 8, validate = {"not_empty"})
	private String remark;

	@ApiModelProperty(value = "点位名称")
	@ExcelProperty(value = "点位名称", index = 9)
	private String pointName;

	@ApiModelProperty(value = "设备编号")
	@ExcelProperty(value = "设备编号", index = 10)
	private String assetCode;

	@ApiModelProperty(value = "分配单位")
	@ExcelProperty(value = "分配单位", index = 11)
	private String allocDepartmentNum;

	@ApiModelProperty(value = "故障级别")
	@ExcelProperty(value = "故障级别", index = 12)
	private String issueLevel;

	private String ticketCodeResult;

	@ApiModelProperty(value = "行号")
	@JSONField(name = "row_index")
	private Integer rowIndex;

	private Integer endIndex;

	private Long pointId;

	private Long assetId;

	private String creator;

	private Date createTime;

	private Date updateTime;

	private Integer isImport;

	private String allocDepartmentNumBak;

}
