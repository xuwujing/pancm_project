package com.zans.mms.vo.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉管理数据导入实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
public class ExcelPoManagerVO implements Serializable {

	@ApiModelProperty(value = "区域")
	@ExcelProperty(value = "区域", index = 1, validate = {"not_empty"})
	private String areaId;

	@ApiModelProperty(value = "类型")
	@ExcelProperty(value = "类型", index = 2)
	private String type;

	@ApiModelProperty(value = "来文分类")
	@ExcelProperty(value = "来文分类", index = 3, validate = {"not_empty"})
	private String eventSource;

	@ApiModelProperty(value = "时间")
	@ExcelProperty(value = "时间", index = 4, validate = {"not_empty"})
	private String breakdownTime;


	@ApiModelProperty(value = "联系方式")
	@ExcelProperty(value = "联系方式", index = 5)
	private String contactInformation;

	@ApiModelProperty(value = "描述内容")
	@ExcelProperty(value = "描述内容", index = 6, validate = {"not_empty"})
	private String problemDescription;


	@ApiModelProperty(value = "行号")
	@JSONField(name = "row_index")
	private Integer rowIndex;

	private Integer endIndex;

	private String pointId;

	private String creator;

	private Date createTime;

	private Date updateTime;


}
