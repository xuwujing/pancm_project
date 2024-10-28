package com.zans.mms.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉管理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Data
@Table(name = "import_log")
@ToString
public class Speed implements Serializable {

	/**
	 * 主键 自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;



	/**
	 * 总条数
	 */
	@Column(name = "total")
	private Integer total;

	/**
	 * 新增条数
	 */
	@Column(name = "add_count")
	private Integer addCount;


	/**
	 * 修改条数
	 */
	@Column(name = "update_count")
	private Integer updateCount;

	/**
	 * 失败条数
	 */
	@Column(name="fail_count")
	private Integer failCount;

	/**
	 * 上传比率
	 */
	@Column(name="rate")
	private BigDecimal rate;



	/**
	 * 创建人
	 */
	@Column(name="creator")
	private String creator;

	/**
	 * 已完成条数
	 */
	@Column(name="success_count")
	private Integer successCount;

	/**
	 * 上传的模块 资产/工单/点位
	 */
	@Column(name="module")
	private String module;

	/**
	 * 文件路径 失败的时候下载使用
	 */
	@Column(name="file_path")
	private String filePath;




}
