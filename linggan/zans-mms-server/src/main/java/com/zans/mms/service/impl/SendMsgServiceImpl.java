package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.SysUserDao;
import com.zans.mms.dao.WorkflowTaskInfoMapper;
import com.zans.mms.service.ISendMsgService;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.vo.push.DropLine;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:消息发送服务层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/10
 */
@Service("sendMsgService")
@Slf4j
public class SendMsgServiceImpl implements ISendMsgService {

	//故障推送给对应的运营商人员
	public static final int COMMON_MSG_DROP_LINE = 41;

	//在线恢复推送
	public static final int COMMON_MSG_ONLINE = 42;



	@Resource
	private WorkflowTaskInfoMapper workflowTaskInfoMapper;

	@Resource
	private SysUserDao sysUserDao;

	@Autowired
	private IWeChatReqService weChatReqService;

	@Override
	public ApiResult sendCommonMsg(DropLine dropLine) {
		if(null==dropLine){
			return ApiResult.error("对象为空！");
		}
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		//如果不为空 且发送类型为1 发送恢复提醒 其他发送掉线提醒
		if(null != dropLine.getSendType()&&dropLine.getSendType()==1){
			weChatPushReqVO.setTemplateType(COMMON_MSG_ONLINE);
		}else {
			dropLine.setSendType(0);
			weChatPushReqVO.setTemplateType(COMMON_MSG_DROP_LINE);
		}

		weChatPushReqVO.setCreator("lgwy");
		List<String> keywords = new ArrayList<>();
		if(dropLine.getSendType()==0){
			if(StringHelper.isEmpty(dropLine.getDropLineType())){
				return ApiResult.error("掉线类型为空！");
			}
			keywords.add(dropLine.getDropLineType());
			keywords.add("设备已掉线超过一个小时，请及时维修");
			if(StringHelper.isEmpty(dropLine.getDropLineTime())){
				return ApiResult.error("掉线时间为空！");
			}
			keywords.add(dropLine.getDropLineTime());
		}else {
			if(StringHelper.isEmpty(dropLine.getIp())){
				return ApiResult.error("IP为空！");
			}
			keywords.add(dropLine.getIp());
			if(StringHelper.isEmpty(dropLine.getPointName())){
				return ApiResult.error("点位名称为空！");
			}
			keywords.add(dropLine.getPointName());
			if(StringHelper.isEmpty(dropLine.getDropLineTime())){
				return ApiResult.error("上线时间为空！");
			}
			keywords.add(dropLine.getDropLineTime());
		}

		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();

		if (StringHelper.isEmpty(dropLine.getPointName())) {
			return ApiResult.error("点位名称为空！");
		}
		jsonObject.put("pointName", dropLine.getPointName());
		if (StringHelper.isEmpty(dropLine.getProjectName())) {
			dropLine.setProjectName("无项目名称");
		}
		jsonObject.put("projectName", dropLine.getProjectName());
		if (StringHelper.isEmpty(dropLine.getIp())) {
			return ApiResult.error("IP传参为空！");
		}
		jsonObject.put("ip", dropLine.getIp());
		//如果为恢复模板 添加掉线类型
		if(null != dropLine.getSendType()&&dropLine.getSendType()==1){
			if(StringHelper.isEmpty(dropLine.getDropLineType())){
				return ApiResult.error("掉线类型为空！");
			}
			jsonObject.put("dropLineType", dropLine.getDropLineType());
		}
		weChatPushReqVO.setJsonObject(jsonObject);
		if(StringHelper.isEmpty(dropLine.getOperator())){
			return ApiResult.error("运营商为空！");
		}
		List<String> currentUsername = sysUserDao.getByOperator(dropLine.getOperator());
//
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
		try {
			ApiResult apiResult = weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
			return apiResult;
		} catch (Exception e) {
			log.error("工单数据推送失败!{}",e);
			return ApiResult.error("推送失败！");
		}

	}


}
