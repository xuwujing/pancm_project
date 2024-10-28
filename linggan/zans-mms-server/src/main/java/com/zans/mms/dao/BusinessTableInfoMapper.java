package com.zans.mms.dao;

import com.zans.mms.dto.workflow.BusinessTableInfoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:业务表信息持久层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/6
 */
@Repository
public interface BusinessTableInfoMapper {
	/**
	 * 根据流程文件id查询业务表信息
	 * @param workflowId 流程文件id
	 * @return
	 */
	BusinessTableInfoDto getTableInfoByWorkflowId(String workflowId);

	/**
	 * 获取状态编码
	 * @param workflowId 流程文件id
	 * @param taskName 任务名称
	 * @return
	 */
	String getStatusByWorkflowIdAndTaskName(@Param("workflowId") String workflowId,@Param("taskName") String taskName);

	/**
	 * 修改状态
	 * @param businessTableName 业务表名
	 * @param businessTableStatusField 业务状态名
	 * @param statusCode 状态编码
	 * @param businessKey 业务主键
	 */
	void updateStatus(@Param("businessTableName") String businessTableName, @Param("businessTableStatusField") String businessTableStatusField,@Param("statusCode") String statusCode,@Param("businessKey") String businessKey);
}
