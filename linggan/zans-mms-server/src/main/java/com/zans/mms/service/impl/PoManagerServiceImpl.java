package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

import java.sql.*;

import com.zans.base.exception.BusinessException;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.util.DateHelper;
import com.zans.base.util.HttpHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.ConnectionManager;
import com.zans.mms.dao.*;
import com.zans.mms.dto.workflow.TicketWorkflowDto;
import com.zans.mms.model.*;
import com.zans.mms.service.*;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.asset.ExcelAssetVO;
import com.zans.mms.vo.asset.ExcelElectricPoliceVO;
import com.zans.mms.vo.asset.subset.AssetExportVO;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.perm.DataPermVO;
import com.zans.mms.vo.po.*;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import com.zans.mms.vo.ticket.TicketSearchReqVO;
import com.zans.mms.vo.user.SysUserVO;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import javafx.beans.property.SetProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static com.zans.base.config.GlobalConstants.*;
import static com.zans.mms.config.MMSConstants.TICKET_TEMPLATE_RECORD;
import static com.zans.mms.config.TicketConstants.OWNER_ROLE;
import static com.zans.mms.config.TicketConstants.SUPERVISION_ROLE;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Service("poManagerService")
@Slf4j
public class PoManagerServiceImpl implements IPoManagerService {

	@Resource
	private TicketDao ticketDao;

	@Resource
	PoManagerDao poManagerDao;

	@Resource
	PoManagerDaoCopy poManagerDaoCopy;

	@Autowired
	ExcelExportUtil excelExportUtil;


	@Value("${api.upload.folder}")
	String uploadFolder;

	@Autowired
	IFileService fileService;

	@Autowired
	WriteErrorMsgUtil writeErrorMsgUtil;

	@Autowired
	IPoBaseDictService poBaseDictService;

	@Autowired
	IPoManagerLogsService poManagerLogsService;

	@Autowired
	ISysUserService sysUserService;

	@Autowired
	BaseVfsDao baseVfsDao;

	@Resource
	private SysUserDao sysUserDao;

	@Autowired
	private IWeChatReqService weChatReqService;

	@Resource
	private WorkflowTaskInfoMapper workflowTaskInfoMapper;

	@Resource
	private BaseOrgMapper baseOrgMapper;

	@Autowired
	SysConstantItemMapper sysConstantItemMapper;

	@Autowired
	DevicePointMapper devicePointMapper;

	@Resource
	private ISerialNumService serialNumService;

	@Autowired
	private IDevicePointService devicePointService;

	@Autowired
	private BaseOrgServiceImpl baseOrgService;


	@Override
	public ApiResult list(PoManagerReqVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<PoManagerRepVO> result = poManagerDao.getList(vo);



		return ApiResult.success(new PageResult<PoManagerRepVO>(page.getTotal(), result, pageSize, pageNum));

	}

	/**
	 * 创建舆情投诉数据
	 * @param poManager
	 * @return
	 */
	@Override
	public ApiResult create(PoManager poManager, UserSession userSession) {
		String poCode = serialNumService.generatePoManagerSerialNum();
		poManager.setPoCode(poCode);
		if(StringUtils.isBlank(poManager.getSolutionTime())){
			poManager.setSolutionTime(null);
		}
		if(StringUtils.isNotBlank(poManager.getDutyContact())){
			poManager.setRepairStatus(6);
		}
		int count = poManagerDao.insertSelective(poManager);
		if(count==1){
			if(StringUtils.isNotBlank(poManager.getDutyContact())){
				this.changeDuty(poManager,userSession,"新建舆情提醒",PO_TEMPLATE_NEW_POMANAGER);
			}
			return ApiResult.success();
		}else {
			return ApiResult.error("创建失败!");
		}
	}


	/**
	 * 创建舆情投诉数据
	 * @param poManager
	 * @return
	 */
	@Override
	public ApiResult update(PoManager poManager,UserSession userSession) {
		if(StringUtils.isBlank(poManager.getSolutionTime())){
			poManager.setSolutionTime(null);
		}
		String username = userSession.getUserName();
		//通过id查询原记录做对比 是否进行状态变更 是否进行负责人变更
		PoManager oldPoManager= this.getView(poManager.getId());
		int count = poManagerDao.updateByPrimaryKeySelective(poManager);
		saveLog(poManager,oldPoManager,1,userSession);
		//判断此舆情是否有关联的重复舆情
		List<Long> ids = poManagerDao.getIds(poManager.getId());
		if(!StringHelper.isEmpty(ids)){
			poManager=poManagerDao.selectByPrimaryKey(poManager.getId());
			for(Long id : ids){
				PoManager temp = new PoManager();
				temp.setId(id);
				temp.setPointId(poManager.getPointId());
				temp.setAreaId(poManager.getAreaId());
				temp.setSolutionTime(StringUtils.isNotBlank(poManager.getSolutionTime())?oldPoManager.getSolutionTime():null);
				temp.setSolution(poManager.getSolution());
				temp.setPoEvent(poManager.getPoEvent());
				temp.setConfirmerDescription(poManager.getConfirmerDescription());
				temp.setClosingPersonDescription(poManager.getClosingPersonDescription());
				temp.setRemark(poManager.getRemark());
				temp.setReason(poManager.getReason());
				temp.setBeforeAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
				temp.setAfterAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
				temp.setFaultPhenomenon(poManager.getFaultPhenomenon());
				//查询原舆情维修前图片
				if(StringUtils.isNotBlank(temp.getBeforeAdjunctId())){
					List<BaseVfs> beforeVfs = baseVfsDao.getByAdjunctId(poManager.getBeforeAdjunctId());
					if(!StringHelper.isEmpty(beforeVfs)){
						for(BaseVfs baseVfs : beforeVfs){
							baseVfs.setAdjunctId(temp.getBeforeAdjunctId());
							baseVfsDao.insertSelective(baseVfs);
						}
					}
				}
				//查询原舆情维修后图片
				if(StringUtils.isNotBlank(temp.getAfterAdjunctId())){
					List<BaseVfs> beforeVfs = baseVfsDao.getByAdjunctId(poManager.getAfterAdjunctId());
					if(!StringHelper.isEmpty(beforeVfs)){
						for(BaseVfs baseVfs : beforeVfs){
							baseVfs.setAdjunctId(temp.getAfterAdjunctId());
							baseVfsDao.insertSelective(baseVfs);
						}
					}
				}
				poManagerDao.updateByPrimaryKeySelective(temp);
			}
		}

		/**
		 * poManager.setRepeatMark(repeatId);
		 * 		poManager.setPointId(oldPoManager.getPointId());
		 * 		poManager.setSolutionTime(StringUtils.isNotBlank(oldPoManager.getSolutionTime())?oldPoManager.getSolutionTime():null);
		 * 		poManager.setSolution(oldPoManager.getSolution());
		 * 		poManager.setPoEvent(oldPoManager.getPoEvent());
		 * 		poManager.setConfirmerDescription(oldPoManager.getConfirmerDescription());
		 * 		poManager.setClosingPersonDescription(oldPoManager.getClosingPersonDescription());
		 * 		poManager.setRemark(oldPoManager.getRemark());
		 * 		poManager.setReason(oldPoManager.getReason());
		 * 		//已结案状态
		 * 		poManager.setRepairStatus(5);
		 * 		poManagerDao.updateByPrimaryKeySelective(poManager);
		 */
		if(count==1){
			return ApiResult.success();
		}else {
			return ApiResult.error("修改失败!");
		}
	}

	private PoManager getView(Long id) {
		return  poManagerDao.selectByPrimaryKey(id);
	}

	/**
	 * 保存日志
	 * @param poManager
	 * @param oldPoManager
	 */
	private void saveLog(PoManager poManager, PoManager oldPoManager,Integer platform,UserSession userSession) {
		String timeout="";
		//判断该舆情是否已超时
		if(isTimeOut(poManager.getId())){
			timeout="(已超时30分钟)";
		}

		//判断原来的维修状态和现在的维修状态是否一致
		PoManagerLogs poManagerLogs = new PoManagerLogs();
		poManagerLogs.setOpPlatform(platform);
		poManagerLogs.setCreator(userSession.getUserName());
		poManagerLogs.setOpType(0);
		poManagerLogs.setPoId(poManager.getId());
		poManagerLogs.setPoStatus(poManager.getRepairStatus());
		//poManagerLogs.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String msg ="";
		if(null!=poManager.getRepairStatus()&&null!=oldPoManager.getRepairStatus()&&!poManager.getRepairStatus().equals(oldPoManager.getRepairStatus())){
			List<SelectVO> statusList = poBaseDictService.findItemsByDict(REPAIR_STATUS);
			String oldStatus="";
			String newStatus="";
			for(SelectVO selectVO : statusList){
				if(Integer.parseInt(selectVO.getItemKey().toString())==oldPoManager.getRepairStatus()){
					oldStatus=selectVO.getItemValue();
				}
				if(Integer.parseInt(selectVO.getItemKey().toString())==poManager.getRepairStatus()){
					newStatus=selectVO.getItemValue();
				}
				if(StringUtils.isNotBlank(oldStatus)&&StringUtils.isNotBlank(newStatus)){
					break;
				}
			}
			msg="#维修状态变更#"+userSession.getNickName()+"将状态由"+oldStatus+"变更为了"+newStatus+timeout;
			poManagerLogs.setMsg(msg);
			poManagerLogsService.insertOne(poManagerLogs);
			//如果状态修改为已派单 判断派工时间是否存在，如果不存在，则进行update
			if(7==poManager.getRepairStatus()){
				PoManager tempPoManager = poManagerDao.selectByPrimaryKey(poManager.getId());
				if(StringUtils.isBlank(tempPoManager.getDispatchTime())){
					//修改一下派工时间
					tempPoManager.setDispatchTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					poManagerDao.updateDispatchTime(tempPoManager);
				}
			}
			///todo 根据目标状态对对应的对象进行推送
			this.pushPoMsg(poManager,userSession);
		}
		if(StringUtils.isBlank(oldPoManager.getDutyContact())&&StringUtils.isNotBlank(poManager.getDutyContact())){
			String currentNickName=sysUserService.findUserByName(poManager.getDutyContact()).getNickName();
			msg="#责任人指派#"+userSession.getNickName()+"将责任人指派为了"+currentNickName;
			poManagerLogs.setMsg(msg);
			poManagerLogsService.insertOne(poManagerLogs);
			//修改状态为已分配
			PoManager temp = new PoManager();
			temp.setId(poManager.getId());
			temp.setRepairStatus(6);
			temp.setSynchronizationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			poManagerDao.updateByPrimaryKeySelective(temp);

		}
		if(StringUtils.isNotBlank(oldPoManager.getDutyContact())&&StringUtils.isNotBlank(poManager.getDutyContact())&&!poManager.getDutyContact().equals(oldPoManager.getDutyContact())){
			String oldNickName=sysUserService.findUserByName(oldPoManager.getDutyContact()).getNickName();
			String currentNickName=sysUserService.findUserByName(poManager.getDutyContact()).getNickName();
			msg="#责任人变更#"+userSession.getNickName()+"将责任人由"+oldNickName+"变更为了"+currentNickName+timeout;
			poManagerLogs.setMsg(msg);
			poManagerLogsService.insertOne(poManagerLogs);
			this.changeDuty(poManager,userSession,msg,PO_TEMPLATE_CHANGE_DUTY);
			PoManager temp = new PoManager();
			temp.setId(poManager.getId());
			temp.setSynchronizationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			poManagerDao.updateByPrimaryKeySelective(temp);
		}
	}

	/**
	 * 责任人变更推送
	 * @param poManager
	 * @param userSession
	 */
	private void changeDuty(PoManager poManager, UserSession userSession,String msg,Integer template) {
		Integer immidiately=poManager.getIsImmidiately();
		poManager=poManagerDao.selectByPrimaryKey(poManager.getId());
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		if(null!=template){
			weChatPushReqVO.setTemplateType(template);
		}else{
			weChatPushReqVO.setTemplateType(PO_TEMPLATE_CHANGE_DUTY);
		}

		weChatPushReqVO.setCreator(userSession.getUserName());
		List<String> keywords = new ArrayList<>();
		keywords.add(StringUtils.isNotBlank(poManager.getPoCode())?poManager.getPoCode():"");
		//根据维修装修查询
		Integer repairStatus = poManager.getRepairStatus();
		String repairStatusName = poBaseDictService.Key2Value(repairStatus,REPAIR_STATUS);
		keywords.add(repairStatusName);
		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();
		List<String> currentUsername = new ArrayList<>();
		currentUsername.add(poManager.getDutyContact());
		//循环一下需要推送的名单
		if(!StringHelper.isEmpty(currentUsername)){
			//比如说 当前是第一包的数据 需要对应推送给第一包所有人 查询下面的所有人进行推送
			List<String> orgUsernameList = poBaseDictService.getOrgUser(currentUsername);
			if(!StringHelper.isEmpty(orgUsernameList)){
				currentUsername.addAll(orgUsernameList);
			}
		}
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
		//todo待修改
		String eventSource = poBaseDictService.Key2Value(poManager.getEventSource(),EVENT_SOURCE);
		if(StringUtils.isBlank(eventSource)){
			eventSource="";
		}
		String road = devicePointMapper.selectPointName(poManager.getPointId());
		if(StringUtils.isBlank(road)){
			road="";
		}
		String poEvent=poBaseDictService.Key2Value(poManager.getPoEvent(),PO_EVENT);
		if(StringUtils.isBlank(poEvent)){
			poEvent="";
		}
		String isImmidiately = ",非紧急舆情";
		if(null==immidiately){
			immidiately=poManager.getIsImmidiately();
			if(immidiately==1){
				isImmidiately=",紧急舆情";
			}
		}
		String remark=poManager.getProblemDescription();
		jsonObject.put("msg",msg);
		jsonObject.put("eventSource",eventSource);
		jsonObject.put("road",road);
		jsonObject.put("poEvent",poEvent);
		jsonObject.put("isImmidiately",isImmidiately);
		jsonObject.put("problemDescription","问题描述："+remark);
		weChatPushReqVO.setJsonObject(jsonObject);
		try {
			weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
		} catch (Exception e) {
			log.error("工单数据推送失败!");
		}
	}

	/**
	 * 判断是否超时
	 * @param id
	 * @return
	 */
	private boolean isTimeOut(Long id) {
		Integer isDispatchTimeOut=poManagerDao.isDispatchTimeOut(id);
		Integer isMaintainTimeOut=poManagerDao.isMaintainTimeOut(id);
		return (isDispatchTimeOut+isMaintainTimeOut)>0;
	}


	@Override
	public ApiResult delete(Long id) {
		poManagerDao.deleteByPrimaryKey(id);
		return ApiResult.success();
	}

	@Override
	public ApiResult view(Long id) {
		Map<String, Object> map =new HashMap<>();
		map.put("view",this.appView(id));
		map.put("logs",poManagerDao.getLogs(id));
		return ApiResult.success(map);
	}


	@Override
	public String export(PoManagerExportReqVO vo, String fileName) {
		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList, "序号","舆情编号", "辖区", "路口", "故障时间", "事件", "事件来源", "类型", "原因","故障现象", "解决时间", "解决方案", "备注");
		//此时写完文件头
		String absoluteNewFilePath = null;
		try {
			absoluteNewFilePath = excelExportUtil.writeHeader("舆情列表", rowNameList, fileName);
		} catch (Exception e) {
			log.error("舆情数据导出时，写文件头错误{}", e);
		}
		List<PoExportVO> exportVOList = poManagerDao.getPoExportData(vo);
		this.writeAssetData(absoluteNewFilePath, exportVOList);
		return absoluteNewFilePath;
	}


	private void writeAssetData(String absoluteNewFilePath, List<PoExportVO> exportVOList) {
		FileInputStream fs = null;
		FileOutputStream out = null;
		try {
			//获取absoluteNewFilePath
			fs = new FileInputStream(absoluteNewFilePath);
			//做2003和2007兼容
			Workbook wb = WorkbookFactory.create(fs);

			//默认取第一个sheet页
			Sheet sheetAt = wb.getSheetAt(0);

			//做数据遍历 进行数据写入、
			//逻辑处理 需修改
			for (int i = 0; i < exportVOList.size(); i++) {
				//首先判断是否为一条重复标记数据 如果为重复标记数据 为其重新赋值
				if(null!=exportVOList.get(i).getRepeatMark()){
					//通过重复标记数据 查询上一层非重复数据 然后为重复数据赋值
					PoManagerExportReqVO vo = new PoManagerExportReqVO();
					vo.setId(exportVOList.get(i).getRepeatMark());
					PoExportVO repeatManager = poManagerDao.getPoExportData(vo).get(0);
					setExportPoProperty(exportVOList.get(i),repeatManager);
				}
				Row row = sheetAt.createRow(i + 1);
				int currentCell = 0;
				//Collections.addAll(rowNameList, "序号", "辖区", "路口", "故障时间", "事件", "事件来源", "类型", "原因", "解决时间", "解决方案", "备注");
				row.createCell(currentCell++).setCellValue(i+1);
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getPoCode());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getAreaId());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getIntersection());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getBreakdownTime());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getPoEvent());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getEventSource());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getPoType());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getReason());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getFaultPhenomenon());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getSolutionTime());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getSolution());
				row.createCell(currentCell++).setCellValue(exportVOList.get(i).getProblemDescription());
			}
			out = new FileOutputStream(absoluteNewFilePath);
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("导出数据写入出现错误！");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("输出流关闭出现异常{}", e);
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					log.error("文件输入流出现异常{}", e);
				}
			}
		}
	}

	/**
	 * 导出的重复数据 做赋值
	 * @param poExportVO
	 * @param repeatManager
	 */
	private void setExportPoProperty(PoExportVO poExportVO, PoExportVO repeatManager) {
		poExportVO.setIntersection(repeatManager.getIntersection());
		poExportVO.setAreaId(repeatManager.getAreaId());
		poExportVO.setReason(repeatManager.getReason());
		poExportVO.setSolutionTime(repeatManager.getSolutionTime());
		poExportVO.setSolution(repeatManager.getSolution());
		if(StringUtils.isBlank(poExportVO.getPoEvent())){
			poExportVO.setPoEvent(repeatManager.getPoEvent());
		}
	}


	@Override
	public ApiResult batchAddPo(String filePath, String fileName, UserSession userSession) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			return ApiResult.error("文件为空!");
		}
		String absoluteNewFilePath = this.uploadFolder + filePath;
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getPoReader());
			if (linkResult == null) {
				return ApiResult.error("未填写任何舆情信息!");
			}
			List<ExcelPoManagerVO> list = linkResult.getEntity().convertToRawTable(ExcelPoManagerVO.class);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何舆情信息!");
			}
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何舆情信息!!");
			}
			log.info("link.size#{}", list.size());
			Map<String, Object> map = this.dealExcelData(list, userSession, absoluteNewFilePath);
			if (!linkResult.getValid()) {
				log.error("文件校验失败#" + absoluteNewFilePath);
				map.put("errorPath", absoluteNewFilePath);
				map.put("msg", "部分数据存在格式错误");
			}

			return ApiResult.success(map);
		} catch (Exception ex) {
			log.error("用户上传模板错误或数据格式有误#" + absoluteNewFilePath, ex);
			return ApiResult.error(5010, "用户上传模板错误，解析失败！");
		}
	}

	private Map<String, Object> dealExcelData(List<ExcelPoManagerVO> list, UserSession userSession, String absoluteNewFilePath) {
		Map<String, Object> map = new HashMap<>();
		String nickname = userSession.getNickName();
		String username = userSession.getUserName();
		List<Integer> remoteCity = new ArrayList<>();
		Collections.addAll(remoteCity,17,11,12,13,14,15,16);
		//定义失败计数
		int failCount = 0;
		//先根据pointCode查询是否有对应的点位信息 如果没有 就不插入 如果有在做数据校验
		for (ExcelPoManagerVO vo : list) {
			//对有非空项不填的处理
			if (vo.toString().contains("[必填]")) {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请补充该行必填项！", absoluteNewFilePath);
				continue;
			}
			//对区域和来文分类做文字转字典
			Integer areaId=poBaseDictService.areaIdValue2Key(vo.getAreaId());
			Integer eventSource=poBaseDictService.eventSourceValue2Key(vo.getEventSource());
			Integer type=poBaseDictService.typeValue2Key(vo.getType());
			PoManager poManager = new PoManager();
			String poCode = serialNumService.generatePoManagerSerialNum();
			poManager.setPoCode(poCode);
			poManager.setCreator(username);
			poManager.setAreaId(areaId);
			poManager.setEventSource(eventSource);
			poManager.setPoType(type);
			String problemDescription=vo.getProblemDescription();
			String contactInfo=vo.getContactInformation();
			if(StringUtils.isNotBlank(contactInfo)){
				String[] split = contactInfo.split(" ");
				if(split.length>0){
					poManager.setContact(split[0]);
				}
				if(split.length>1){
					poManager.setContactPhone(split[split.length-1]);
				}
			}
			poManager.setProblemDescription(problemDescription);
			poManager.setBreakdownTime(vo.getBreakdownTime());
			try {
				//看辖区属于哪个区域 用于自动分配
				String belong =null;
				String area=null;
				///todo 如果是配时问题 或故障问题 自动分配对应的人 并推送
				if(StringUtils.isNotBlank(vo.getType())&&"配时".equals(vo.getType())){
					 belong = poBaseDictService.getAreaBelong(areaId);
					if(StringUtils.isNotBlank(belong)){
						if(belong.equals("1")){
							area="10001";
						}
						if(belong.equals("2")){
							area="10002";
						}
						if(belong.equals("3")){
							area="10003";
						}
					}
					if(StringUtils.isNotBlank(area)&&("10002".equals(area)||"10003".equals(area))&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
						poManager.setDutyContact("gd0002");
						poManager.setRepairStatus(6);
					}
					if(StringUtils.isNotBlank(area)&&"10001".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
						poManager.setDutyContact("hx0014");
						poManager.setRepairStatus(6);
					}

				}else if(StringUtils.isNotBlank(vo.getType())&&"故障".equals(vo.getType())){
					belong = poBaseDictService.getAreaBelong(areaId);
					if(StringUtils.isNotBlank(belong)){
						if(belong.equals("1")){
							area="10001";
						}
						if(belong.equals("2")){
							area="10002";
						}
						if(belong.equals("3")){
							area="10003";
						}
					}
					if(StringUtils.isNotBlank(area)){
						if("10001".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
							poManager.setRepairStatus(6);
							poManager.setDutyContact("bsy025");
						}
						if("10002".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
							poManager.setRepairStatus(6);
							poManager.setDutyContact("sl005");
						}
						if("10003".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
							poManager.setRepairStatus(6);
							poManager.setDutyContact("hx0003");
						}
					}
				}
				if(StringUtils.isBlank(poManager.getDutyContact())){
					poManager.setRepairStatus(6);
					poManager.setDutyContact("wangh");
				}

				poManagerDao.insertSelective(poManager);
			}catch (Exception e){
				log.error("导入错误原因,{}",e);
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请补充该行必填项或检查数据格式！", absoluteNewFilePath);
			}


		}
		map.put("failCount", failCount);
		map.put("successCount", list.size() - failCount);
		if (list.size() != list.size() - failCount) {
			map.put("errorPath", absoluteNewFilePath);
		}
		return map;
	}

	private ExcelSheetReader getPoReader() {
		return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
				.notNullFields(Lists.newArrayList("areaId,eventSource"))
				.headerClass(ExcelPoManagerVO.class).build();
	}

	private File getRemoteFile(String filePath) {
		if (filePath == null) {
			return null;
		}
		File file = new File(this.uploadFolder + "/" + filePath);
		if (!file.exists()) {
			log.error("file error#" + this.uploadFolder + "/" + filePath);
			return null;
		}
		return file;
	}



	/** ----------------------------------小程序相关方法开始----------------------------------------**/
	/**
	 * 小程序列表页查询
	 * @param vo
	 * @return
	 */
	@Override
	public ApiResult appList(PoManagerReqVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		if(StringUtils.isBlank(vo.getKeyword())){
			vo.setKeywordList(new ArrayList<>());
		}else{
			String[] split = vo.getKeyword().split(" ");
			List<String> keywordList= new ArrayList<>();
			for(int i =0;i<split.length;i++){
				if(StringUtils.isNotBlank(split[i])){
					keywordList.add(split[i]);
				}
			}
			vo.setKeywordList(keywordList);
		}
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<PoManager> result = poManagerDao.getAppList(vo);


		return ApiResult.success(new PageResult<PoManager>(page.getTotal(), result, pageSize, pageNum));
	}

	/**
	 * 小程序详情
	 * @param id
	 * @return
	 */
	@Override
	public ApiResult appView(Long id) {
		PoManagerRepVO poManager = poManagerDao.appView(id);
		//通过重复的id去查code
		if(StringUtils.isNotBlank(poManager.getRepeatMark())){
			String code = poManagerDao.selectByPrimaryKey(Long.parseLong(poManager.getRepeatMark())).getPoCode();
			poManager.setRepeatCode(code);
		}
		//维修前图片查询
		List<BaseVfs> beforeBaseVfsList=baseVfsDao.getPoImg(poManager.getBeforeAdjunctId());
		//维修后图片查询
		List<BaseVfs> afterBaseVfsList=baseVfsDao.getPoImg(poManager.getAfterAdjunctId());
		poManager.setBeforeBaseVfsList(beforeBaseVfsList);
		poManager.setAfterBaseVfsList(afterBaseVfsList);
		return ApiResult.success(poManager);
	}

	@Override
	public ApiResult imgUpdate(PoManagerReqVO vo) {
		poManagerDao.updateBase(vo);
		return ApiResult.success();
	}

	/**
	 * 舆情日志
	 * @param id
	 * @return
	 */
	@Override
	public ApiResult getLogs(Long id) {
		List<PoManagerLogs> poManagerLogsList=poManagerDao.getLogs(id);
		return ApiResult.success(poManagerLogsList);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ApiResult saveLog(PoManagerLogs poManagerLogs,UserSession userSession) {
		PoManager poManager = new PoManager();
		poManager.setId(poManagerLogs.getPoId());
		poManager.setRemark(poManagerLogs.getMsg());
		String msg="#维修上报#"+poManagerLogs.getMsg();
		if(StringUtils.isNotBlank(poManagerLogs.getMsg())){
			poManager.setRemark(poManagerLogs.getMsg());
			//根据poManager判断是否需要维护维修时间
			PoManager old = poManagerDao.selectByPrimaryKey(poManagerLogs.getPoId());
			if(StringUtils.isBlank(old.getMaintainTime())){
				poManager.setMaintainTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
			poManagerDao.updateByPrimaryKeySelective(poManager);
		}
		poManagerLogs.setMsg(msg);
		if(poManagerLogs.getPoStatus()==3){
			poManager.setRepairStatus(poManagerLogs.getPoStatus());
			PoManager oldPoManager= this.getView(poManager.getId());
			saveLog(poManager,oldPoManager,2,userSession);
			poManagerDao.updateByPrimaryKeySelective(poManager);
		}else{
			poManager.setRepairStatus(2);
			poManagerDao.updateByPrimaryKeySelective(poManager);
			poManagerLogsService.insertOne(poManagerLogs);
		}
		return ApiResult.success();
	}

	@Override
	public List<CircleUnit> getAppPoManagerTotal(UserSession userSession) {
	/*	DataPermVO dataPerm = dataPermService.getTopDataPerm(userSession);
		TicketSearchReqVO ticketSearchReqVO = new TicketSearchReqVO();
		ticketSearchReqVO.setDataPerm(dataPerm.getDataPerm());
		ticketSearchReqVO.setOrgId(userSession.getOrgId());*/

		String roleId = userSession.getRoleId();
		String maintainNum="";
		if("0301".equals(roleId)||"0302".equals(roleId)){
			maintainNum=userSession.getOrgId();
		}
		List<CircleUnit> unitList = poManagerDao.getAppPoManagerTotal(maintainNum);
		return unitList;
	}

	/**
	 * 舆情微信推送
	 * @param poManager
	 */
	private Boolean pushPoMsg(PoManager poManager,UserSession userSession) {
		//根据repairStatus来确认推送模板和内容
		//待推送unionId列表 由于需要根据每个人定制查询推送数据 查询所有的监理和业主账号
		String username = "";
		switch (poManager.getRepairStatus()){
			case 3:
				//修改为维修完成状态 推送给对应的责任人
				if(StringUtils.isBlank(poManager.getDutyContact())){
					username=poManagerDao.selectByPrimaryKey(poManager.getId()).getDutyContact();
				}else{
					username=poManager.getDutyContact();
				}
				pushToDuty(poManager,userSession);
				break;

			case 4:
				//修改为已确认状态 推送 给王慧
				username="wangh";
				pushToClosingPerson(poManager,userSession);
				break;

			default:
				break;
		}
		//日志记录
		if(StringUtils.isNotBlank(username)){
			PoManagerLogs poManagerLogs = new PoManagerLogs();
			poManagerLogs.setPoId(poManager.getId());
			poManagerLogs.setPoStatus(poManager.getRepairStatus());
			List<String> usernameList = new ArrayList<>();
			usernameList.add(username);
			String names=sysUserDao.getNames(usernameList);
			String msg="推送舆情消息给:"+names;
			poManagerLogs.setMsg(msg);
			poManagerLogs.setCreator(userSession.getUserName());
			poManagerLogsService.insertOne(poManagerLogs);
		}

		return true;
	}

	/**
	 * 推送给结案人
	 * @param poManager
	 * @param userSession
	 */
	@Override
	public void pushToClosingPerson(PoManager poManager, UserSession userSession) {
		Integer immidiately=poManager.getIsImmidiately();
		poManager=poManagerDao.selectByPrimaryKey(poManager.getId());
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		weChatPushReqVO.setTemplateType(PO_TEMPLATE_CLOSING_PERSON);
		weChatPushReqVO.setCreator(userSession.getUserName());
		List<String> keywords = new ArrayList<>();
		keywords.add(StringUtils.isNotBlank(poManager.getPoCode())?poManager.getPoCode():"");
		keywords.add("已确认");
		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();
		List<String> currentUsername = new ArrayList<>();
		currentUsername.add("wangh");
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
		//todo待修改
		String dutyContact=sysUserService.findUserByName(poManager.getDutyContact()).getNickName();
		String eventSource = poBaseDictService.Key2Value(poManager.getEventSource(),EVENT_SOURCE);
		if(StringUtils.isBlank(eventSource)){
			eventSource="";
		}
		String road = devicePointMapper.selectPointName(poManager.getPointId());
		if(StringUtils.isBlank(road)){
			road="";
		}
		String poEvent=poBaseDictService.Key2Value(poManager.getPoEvent(),PO_EVENT);
		if(StringUtils.isBlank(poEvent)){
			poEvent="";
		}
		String isImmidiately = ",非紧急舆情";
		if(null==immidiately){
			immidiately=poManager.getIsImmidiately();
			if(immidiately==1){
				isImmidiately=",紧急舆情";
			}
		}
		String remark=poManager.getProblemDescription();
		/*if(StringUtils.isNotBlank(poManager.getProblemDescription())){
			remark=poManager.getProblemDescription().split(" ")[poManager.getProblemDescription().split(" ").length-1];
		}*/
		String solution=poManager.getSolution();
		if(StringUtils.isBlank(solution)){
			solution="";
		}
		jsonObject.put("dutyContact",dutyContact);
		jsonObject.put("now",DateHelper.getNow());
		jsonObject.put("eventSource",eventSource);
		jsonObject.put("road",road);
		jsonObject.put("poEvent",poEvent);
		jsonObject.put("isImmidiately",isImmidiately);
		jsonObject.put("problemDescription","问题描述："+remark);
		jsonObject.put("solution",solution);
		weChatPushReqVO.setJsonObject(jsonObject);
		try {
			weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
		} catch (Exception e) {
			log.error("工单数据推送失败!");
		}
	}

	/**
	 * 推送给责任人
	 * @param poManager
	 * @param userSession
	 */
	@Override
	public void pushToDuty(PoManager poManager, UserSession userSession) {
		Integer immidiately=poManager.getIsImmidiately();
		poManager=poManagerDao.selectByPrimaryKey(poManager.getId());
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		weChatPushReqVO.setTemplateType(PO_TEMPLATE_DUTY);
		weChatPushReqVO.setCreator("lgwy");
		List<String> keywords = new ArrayList<>();
		String orgName=baseOrgMapper.queryByOrgId(userSession.getOrgId()).getOrgName();
		keywords.add(userSession.getNickName()+"("+orgName+")");
		keywords.add(DateHelper.getNow());
		String eventSource = poBaseDictService.Key2Value(poManager.getEventSource(),EVENT_SOURCE);
		if(StringUtils.isBlank(eventSource)){
			eventSource="";
		}
		String areaName = poBaseDictService.Key2Value(poManager.getAreaId(),AREA_ID);
		if(StringUtils.isBlank(areaName)){
			areaName="";
		}
		String event= poBaseDictService.Key2Value(poManager.getPoEvent(),PO_EVENT);
		if(StringUtils.isBlank(event)){
			event="";
		}
		String isImmidiately = ",非紧急舆情";
		if(null==immidiately){
			immidiately=poManager.getIsImmidiately();
			if(immidiately==1){
				isImmidiately=",紧急舆情";
			}
		}


		/*if(StringUtils.isNotBlank(poManager.getProblemDescription())){
			remark=poManager.getProblemDescription().split(" ")[poManager.getProblemDescription().split(" ").length-1];
		}*/
		String roadName = devicePointMapper.selectPointName(poManager.getPointId());
		if(StringUtils.isBlank(roadName)){
			roadName="";
		}
		String problemDescription=poManager.getProblemDescription();
		if(StringUtils.isBlank(problemDescription)){
			problemDescription="";
		}
		String solution= poManager.getSolution();
		if(StringUtils.isBlank(solution)){
			solution="";
		}
		//keywords.add("该舆情来自"+eventSource+":"+areaName+roadName+event+isImmidiately+"。"+"解决方案:"+poManager.getSolution()+"。");
		String remark="该舆情来自"+eventSource+":"+areaName+roadName+event+isImmidiately+"。"+"问题描述："+problemDescription+"解决方案:"+solution+"。";
		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();
		List<String> currentUsername = new ArrayList<>();
		currentUsername.add(poManager.getDutyContact());
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
		//todo待修改
		jsonObject.put("remark",remark);
		weChatPushReqVO.setJsonObject(jsonObject);
		try {
			weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
		} catch (Exception e) {
			log.error("工单数据推送失败!");
		}
	}


	/**
	 * 推送给外场维修人员 手动推送
	 * @param vo
	 * @param userSession
	 */
	@Override
	public ApiResult pushToRepair(PoManagerPushReqVO vo, UserSession userSession) {
		//先通过id查询poManager数据 拿到对应的外场维修人员账号
		if(null==vo||null==vo.getId()){
			throw new BusinessException("传参未带业务主键！");
		}
		/*//先做一次update保存推送人的用户名
		if(null !=poManager.getRepairPersonList() && poManager.getRepairPersonList().size()>0){
			for(int i =0,;i<poManager.getRepairPersonList().size();i++){

			}
		}*/
		//poManagerDao.updateRepairPerson(vo);

		PoManager poManager=poManagerDao.selectByPrimaryKey(vo.getId());
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		weChatPushReqVO.setTemplateType(PO_TEMPLATE_REPAIR_PERSON);
		weChatPushReqVO.setCreator(userSession.getUserName());
		List<String> keywords = new ArrayList<>();
		keywords.add("舆情处理");
		keywords.add(DateHelper.getNow());
		String eventSource = poBaseDictService.Key2Value(poManager.getEventSource(),EVENT_SOURCE);
		String areaName = poBaseDictService.Key2Value(poManager.getAreaId(),AREA_ID);
		String event= poBaseDictService.Key2Value(poManager.getPoEvent(),PO_EVENT);
		String isImmidiately = "非紧急舆情";
		if(poManager.getIsImmidiately()==1){
			isImmidiately="紧急舆情";
		}
		String remark=poManager.getProblemDescription();
		/*if(StringUtils.isNotBlank(poManager.getProblemDescription())){
			remark=poManager.getProblemDescription().split(" ")[poManager.getProblemDescription().split(" ").length-1];
		}*/
		String roadName = devicePointMapper.selectPointName(poManager.getPointId());
		keywords.add("该舆情来自"+eventSource+":"+areaName+roadName+event+","+isImmidiately+"。");
		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();
		List<String> currentUsername = vo.getRepairPersonList();
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
		jsonObject.put("remark",remark);
		weChatPushReqVO.setJsonObject(jsonObject);
		weChatPushReqVO.setSuffix("id="+poManager.getId());
		try {
			ApiResult apiResult = weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
			//日志记录
			if(null !=vo.getRepairPersonList()&&vo.getRepairPersonList().size()>0){
				PoManagerLogs poManagerLogs = new PoManagerLogs();
				poManagerLogs.setPoId(poManager.getId());
				poManagerLogs.setPoStatus(poManager.getRepairStatus());
				String names=sysUserDao.getNames(vo.getRepairPersonList());
				String msg="推送舆情消息给:"+names;
				poManagerLogs.setMsg(msg);
				poManagerLogs.setCreator(userSession.getUserName());
				poManagerLogsService.insertOne(poManagerLogs);
			}
			/**
			 * 舆情状态为"确认中"，点击舆情推送后，状态自动修改为"已派单" 。
			 */
			if(6==poManager.getRepairStatus()){
				PoManager temp = new PoManager();
				temp.setId(poManager.getId());
				temp.setRepairStatus(7);
				this.update(temp,userSession);
			}
			return apiResult;
		} catch (Exception e) {
			log.error("数据推送失败!");
			return ApiResult.error("推送失败！");
		}
	}

	/**
	 * 推送给所有责任人
	 * @param vo 全部责任人推送
	 */
	@Override
	public void pushToAllDuty(PoManagerPushVO vo) {
		WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
		weChatPushReqVO.setTemplateType(PO_TEMPLATE_ALL_DUTY);
		weChatPushReqVO.setCreator("lgwy");
		List<String> keywords = new ArrayList<>();
		keywords.add("交管局舆情通知");
		keywords.add("民意云平台");
		keywords.add(DateHelper.getNow());
		keywords.add("新舆情提醒，本次接受舆情消息"+vo.getTotal()+"起，请及时处理！");
		weChatPushReqVO.setKeywords(keywords);
		JSONObject jsonObject = new JSONObject();
		List<String> currentUsername =vo.getDutyList();
		//循环一下需要推送的名单
		if(!StringHelper.isEmpty(currentUsername)){
			//比如说 当前是第一包的数据 需要对应推送给第一包所有人 查询下面的所有人进行推送
			List<String> orgUsernameList = poBaseDictService.getOrgUser(currentUsername);
			if(!StringHelper.isEmpty(orgUsernameList)){
				currentUsername.addAll(orgUsernameList);
			}
		}
		//写死推送给零感网御工作人员和王慧
		if(!currentUsername.contains("wangh")){
			currentUsername.add("wangh");
		}
		currentUsername.add("lgwy-test");
		currentUsername.add("beiso");
		currentUsername.add("qitain");
		List<String> unionidList = workflowTaskInfoMapper.getUserByApproved(currentUsername);
		weChatPushReqVO.setUnionIdList(unionidList);
/*		//todo待修改
		jsonObject.put("time",DateHelper.getNow());*/
		/*jsonObject.put("total",vo.getTotal());
		jsonObject.put("assigned",vo.getAssigned());
		jsonObject.put("unassigned",vo.getUnassigned());*/
		weChatPushReqVO.setJsonObject(jsonObject);
		try {
			weChatReqService.weChatPushWorkFLow(weChatPushReqVO);
		} catch (Exception e) {
			log.error("工单数据推送失败!{}",e);
		}
	}

	@Override
	public ApiResult getRepairPerson(Integer areaId) {
		//根据areaId确定辖区
		String belong = poBaseDictService.getAreaBelong(areaId);
		String area=null;
		if(StringUtils.isNotBlank(belong)){
			if(belong.equals("1")){
				area="10001";
			}
			if(belong.equals("2")){
				area="10002";
			}
			if(belong.equals("3")){
				area="10003";
			}
		}
		List<SysUserVO> repairPersonList = sysUserService.getRepairPersonByAreaId(area);
		return ApiResult.success(repairPersonList);
	}

	/** ----------------------------------小程序相关方法结束----------------------------------------**/




	/** ---------------------------------模板常量-----------------------------------------------**/
	//责任人变更推送
	public static final int PO_TEMPLATE_NEW_POMANAGER = 36;
	//责任人变更推送
	public static final int PO_TEMPLATE_CHANGE_DUTY = 35;
	//给结案人推送
	public static final int PO_TEMPLATE_CLOSING_PERSON = 34;

	//给维修完成责任人推送推送
	public static final int PO_TEMPLATE_DUTY = 33;

	//变为维修中状态 手动推送给外场
	public static final int PO_TEMPLATE_REPAIR_PERSON = 32;

	//调接口拿到新数据推送给所有人
	public static final int PO_TEMPLATE_ALL_DUTY = 31;



	/** ---------------------------------模板常量-----------------------------------------------**/


	/**
	 * 校验问题编号唯一
	 * @param poManager
	 * @return
	 */
	@Override
	public ApiResult check(PoManager poManager) {
		Boolean flag=poManagerDao.check(poManager)<1;
		return ApiResult.success(flag);
	}


	/**
	 * 从远程获取数据
	 */
	@Override
	public void getDataFormRemoteTask() {
		String url = sysConstantItemMapper.getConstantValueByKey("jdbcurl");
		if(StringUtils.isBlank(url)){
			url="jdbc:oracle:thin:@27.10.21.10:10036/otdb";
		}
		String name="chb_lg";
		String pwd="lg#2021";
		Connection connection = null;
		PreparedStatement statement=null;
		ResultSet rs=null;
		String enid=sysConstantItemMapper.getConstantValueByKey("enid");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection= DriverManager.getConnection(url,name,pwd);
			String sql=sysConstantItemMapper.getConstantValueByKey("sql");
			if(StringUtils.isBlank(sql)){
				sql="select tc.*,lw.name as LWFLNAME,tp.name as TName,tu.ORG_NAME as ORGNAME from LBX_MYUN.lg_task_case tc left join LBX_MYUN.task_lw lw on tc.LWFL=lw.id\n" +
						"left join LBX_MYUN.task_problem  tp on tp.id =tc.wtfl1        left join   LBX_MYUN.task_unit tu on tu.enid=tc.duty_dept\n" +
						"where tc.SQSJ > to_date('2021-08-11 12:00:00','YYYY/MM/DD HH24:MI:SS')  and    (tu.ORG_NAME  like '%大队%' or tu.ORG_NAME ='科技管理处' )\n" +
						"and tc.wtfl1 in (30,31,56,57,58,62,63) ";
			}
			String sql2=" and tc.enid > %s order by tc.enid asc";
			log.info(sql+String.format(sql2,enid));
			statement= connection.prepareStatement(sql+String.format(sql2,enid));
			rs=statement.executeQuery();
			log.info("数据完成读取！");
			List<PoRemoteDataVO> poRemoteDataVOList = new ArrayList<>();
			Long maxEnid=0L;
			while(rs.next()){
				PoRemoteDataVO poRemoteDataVO=new PoRemoteDataVO();
				poRemoteDataVO.setEnid(rs.getLong("ENID"));
				poRemoteDataVO.setAjbh(rs.getString("AJBH"));
				poRemoteDataVO.setSqsj(rs.getString("SQSJ"));
				poRemoteDataVO.setLwfl(rs.getString("LWFL"));
				poRemoteDataVO.setLwfl2(rs.getString("LWFL2"));
				poRemoteDataVO.setDsrxm(rs.getString("DSRXM"));
				poRemoteDataVO.setDsrdh(rs.getString("DSRDH"));
				poRemoteDataVO.setXzqh(rs.getString("XZQH"));
				poRemoteDataVO.setWtfl(rs.getString("WTFL"));
				poRemoteDataVO.setWtfl1(rs.getString("WTFL1"));
				poRemoteDataVO.setWtfl2(rs.getString("WTFL2"));
				poRemoteDataVO.setGdzt(rs.getInt("GDZT"));
				poRemoteDataVO.setDutyDept(rs.getString("DUTY_DEPT"));
				poRemoteDataVO.setLwflName(rs.getString("LWFLNAME"));
				poRemoteDataVO.setTName(rs.getString("TNAME"));
				poRemoteDataVO.setWtnr(rs.getString("WTNR"));
				poRemoteDataVO.setOrgName(rs.getString("ORGNAME"));
				poRemoteDataVOList.add(poRemoteDataVO);

			}
			//成功计数
			int successCount=0;
			//失败计数
			int failCount=0;
			//分配计数
			int assignCount=0;
			//未分配计数
			int unAssignCount=0;
			List<String> dutyList = new ArrayList<>();
			if(null!=poRemoteDataVOList&&poRemoteDataVOList.size()>0){
				maxEnid=poRemoteDataVOList.get(poRemoteDataVOList.size()-1).getEnid();
				//做一次update 修改当前最大的enid
				sysConstantItemMapper.updateByConstantKey("enid",maxEnid);
				//遍历一次数据 做数据插入

				for(PoRemoteDataVO poRemoteDataVO : poRemoteDataVOList){
					PoManager poManager = new PoManager();
					String poCode = serialNumService.generatePoManagerSerialNum();
					poManager.setPoCode(poCode);
					setPoProperty(poManager,poRemoteDataVO);
					try {
						log.info("开始新增数据");
						poManagerDao.insertSelective(poManager);
						successCount++;
						log.info("当前成功条数{}",successCount);
						if(StringUtils.isNotBlank(poManager.getDutyContact())){
							//责任人累加
							if(!dutyList.contains(poManager.getDutyContact())){
								dutyList.add(poManager.getDutyContact());
							}
							assignCount++;
							log.info("当前分配条数{}",assignCount);
						}else{
							unAssignCount++;
							log.info("当前未分配条数{}",assignCount);
						}
					}catch (Exception e){
						failCount++;
						log.error("插入一条舆情数据失败,数据:{}",poManager.toString());
					}
				}
			}
			//分配情况推送
			if(0!=successCount){
				PoManagerPushVO poManagerPushVO = new PoManagerPushVO();
				poManagerPushVO.setTotal(successCount);
				poManagerPushVO.setAssigned(assignCount);
				poManagerPushVO.setUnassigned(unAssignCount);
				poManagerPushVO.setDutyList(dutyList);
				this.pushToAllDuty(poManagerPushVO);
			}

			//数据校验 查询今天的数据应该有哪些 查询是否已插入 如果没有插入 做一下补录
			String sqsj=sysConstantItemMapper.getTimeByKey("sqsj");
			String maxEnid1 = sysConstantItemMapper.getConstantValueByKey("enid");
			String sql3=sysConstantItemMapper.getConstantValueByKey("sql3");
			if(StringUtils.isBlank(sql3)){
				sql3="select tc.*,lw.name as LWFLNAME,tp.name as TName,tu.ORG_NAME as ORGNAME from LBX_MYUN.lg_task_case tc left join LBX_MYUN.task_lw lw on tc.LWFL=lw.id\n" +
						"left join LBX_MYUN.task_problem  tp on tp.id =tc.wtfl1        left join   LBX_MYUN.task_unit tu on tu.enid=tc.duty_dept\n" +
						"where tc.SQSJ > to_date((SELECT To_char(Trunc(SYSDATE), 'yyyy/mm/dd hh24:mi:ss') FROM dual),'YYYY/MM/DD HH24:MI:SS')  \n" +
						"and    (tu.ORG_NAME  like '%大队%' or tu.ORG_NAME ='科技管理处' or tu.ORG_NAME is null) \n" +
						"and (tc.wtfl1 in (30,31,56,57,58,62,63) or tc.wtfl1  is null  ) \n" +
						"and (tc.wtnr like '%信号灯%' or tc.wtnr like '%红绿灯%' or tc.wtnr like '%示屏%' or tc.wtnr like '%电子屏%' or tc.wtnr like '%电子显示屏%' or tc.wtnr like '%LED显示屏%') order by sqsj asc";
			}
			String sql4=" and tc.SQSJ  > to_date('%s','YYYY/MM/DD HH24:MI:SS') ";
			String sql5=" and  tc.enid < %s order by sqsj asc";
			log.info(sql3+String.format(sql4,sqsj)+String.format(sql5,maxEnid1));
			statement= connection.prepareStatement(sql3+String.format(sql4,sqsj)+String.format(sql5,maxEnid1));
			rs=statement.executeQuery();
			log.info("补录数据完成读取！");
			List<PoRemoteDataVO> poRemoteTimeDataVOList = new ArrayList<>();
			while(rs.next()){
				PoRemoteDataVO poRemoteDataVO=new PoRemoteDataVO();
				poRemoteDataVO.setEnid(rs.getLong("ENID"));
				poRemoteDataVO.setAjbh(rs.getString("AJBH"));
				poRemoteDataVO.setSqsj(rs.getString("SQSJ"));
				poRemoteDataVO.setLwfl(rs.getString("LWFL"));
				poRemoteDataVO.setLwfl2(rs.getString("LWFL2"));
				poRemoteDataVO.setDsrxm(rs.getString("DSRXM"));
				poRemoteDataVO.setDsrdh(rs.getString("DSRDH"));
				poRemoteDataVO.setXzqh(rs.getString("XZQH"));
				poRemoteDataVO.setWtfl(rs.getString("WTFL"));
				poRemoteDataVO.setWtfl1(rs.getString("WTFL1"));
				poRemoteDataVO.setWtfl2(rs.getString("WTFL2"));
				poRemoteDataVO.setGdzt(rs.getInt("GDZT"));
				poRemoteDataVO.setDutyDept(rs.getString("DUTY_DEPT"));
				poRemoteDataVO.setLwflName(rs.getString("LWFLNAME"));
				poRemoteDataVO.setTName(rs.getString("TNAME"));
				poRemoteDataVO.setWtnr(rs.getString("WTNR"));
				poRemoteDataVO.setOrgName(rs.getString("ORGNAME"));
				poRemoteTimeDataVOList.add(poRemoteDataVO);

			}
			//成功计数
			int timeSuccessCount=0;
			//失败计数
			int timeFailCount=0;
			//分配计数
			int timeAssignCount=0;
			//未分配计数
			int timeUnAssignCount=0;
			List<String> timeDutyList = new ArrayList<>();
			if(null!=poRemoteTimeDataVOList&&poRemoteTimeDataVOList.size()>0){
				String maxSqsj = poRemoteTimeDataVOList.get(poRemoteTimeDataVOList.size() - 1).getSqsj();
				sysConstantItemMapper.updateStringByConstantKey("sqsj",maxSqsj);
				//遍历一次数据 做数据插入
				for(PoRemoteDataVO poRemoteDataVO : poRemoteTimeDataVOList){
					PoManager poManager = new PoManager();
					String poCode = serialNumService.generatePoManagerSerialNum();
					poManager.setPoCode(poCode);
					setPoProperty(poManager,poRemoteDataVO);
					try {
						if(StringUtils.isNotBlank(poManager.getBreakdownTime())&&isNotExist(poManager.getBreakdownTime(), poManager.getOriginalProblemId())){
							log.info("开始新增补录数据");
							poManagerDao.insertSelective(poManager);
							timeSuccessCount++;
							log.info("当前成功补录条数{}", successCount);
							if (StringUtils.isNotBlank(poManager.getDutyContact())) {
								//责任人累加
								if(!timeDutyList.contains(poManager.getDutyContact())){
									timeDutyList.add(poManager.getDutyContact());
								}
								timeAssignCount++;
								log.info("当前补录分配条数{}", assignCount);
							} else {
								timeUnAssignCount++;
								log.info("当前补录未分配条数{}", assignCount);
							}
						}
					}catch (Exception e){
						timeFailCount++;
						log.error("插入一条补录舆情数据失败,数据:{}",poManager.toString());
					}
				}
			}
			//分配情况推送
			if(0!=timeSuccessCount){
				PoManagerPushVO poManagerPushVO = new PoManagerPushVO();
				poManagerPushVO.setTotal(timeSuccessCount);
				poManagerPushVO.setAssigned(timeAssignCount);
				poManagerPushVO.setUnassigned(timeUnAssignCount);
				poManagerPushVO.setDutyList(timeDutyList);
				this.pushToAllDuty(poManagerPushVO);
			}

		}catch (Exception e){
			log.error("数据库连接失败！{}",e);
			throw new BusinessException("数据库连接失败！");
		}finally {
			close(rs,statement,connection);
		}





	}

	private boolean isNotExist(String breakdownTime,String originalProblemId) {
		return poManagerDao.isNotExist(breakdownTime,originalProblemId) ==0;
	}

	/**
	 * 设置舆情数据
	 * @param poManager
	 * @param poRemoteDataVO
	 */
	private void setPoProperty(PoManager poManager, PoRemoteDataVO poRemoteDataVO) {
		//设置舆情数据 进行自动分配
		//案件编号
		poManager.setOriginalProblemId(StringUtils.isNotBlank(poRemoteDataVO.getAjbh())?poRemoteDataVO.getAjbh():"");
		//创建人
		poManager.setCreator("lgwy");
		//远城区
		List<Integer> remoteCity = new ArrayList<>();
		Collections.addAll(remoteCity,17,11,12,13,14,15,16);
		//问题描述的辖区名称
		List<SelectVO> areaIdList = poBaseDictService.findItemsByDict(AREA_ID);
		areaIdList.remove(0);
		areaIdList.remove(0);
		poManager.setProblemDescription(StringUtils.isNotBlank(poRemoteDataVO.getWtnr())?poRemoteDataVO.getWtnr():"");
		String area=null;
		///todo根据行政区划 设置辖区 并分辨出所属区域
		if(StringUtils.isNotBlank(poRemoteDataVO.getXzqh())){
			PoBaseDict poBaseDict = poBaseDictService.findByCode(poRemoteDataVO.getXzqh());
			if(null!=poBaseDict&&StringUtils.isNotBlank(poBaseDict.getItemKey())){
				poManager.setAreaId(Integer.parseInt(poBaseDict.getItemKey()));
			}
			try {
				//如果没有匹配上辖区 用问题描述内得内容去匹配
				if(null==poManager.getAreaId()&&!StringHelper.isEmpty(poRemoteDataVO.getWtnr())){
					for(SelectVO selectVO : areaIdList){
						if(poRemoteDataVO.getWtnr().contains(selectVO.getItemValue())){
							poManager.setAreaId((int)selectVO.getItemKey());
							break;
						}
					}
				}
			}catch (Exception e){
				log.error("模糊匹配辖区报错！");
			}

			if(null!=poBaseDict&&StringUtils.isNotBlank(poBaseDict.getBelong())){
				if(poBaseDict.getBelong().equals("1")){
					area="10001";
				}
				if(poBaseDict.getBelong().equals("2")){
					area="10002";
				}
				if(poBaseDict.getBelong().equals("3")){
					area="10003";
				}
			}
		}
		//申请时间
		poManager.setBreakdownTime(StringUtils.isNotBlank(poRemoteDataVO.getSqsj())?poRemoteDataVO.getSqsj(): new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//设备故障类型 如果不是配时问题 全部设为故障
		if(StringUtils.isNotBlank(poRemoteDataVO.getWtfl1())&&"31".equals(poRemoteDataVO.getWtfl1())){
			poManager.setPoType(2);
		}else{
			poManager.setPoType(1);
		}
		if(StringUtils.isNotBlank(poRemoteDataVO.getWtfl1())){
			//根据故障类型设定事件
			Integer poEvent=poBaseDictService.remark2ItemKey(poRemoteDataVO.getWtfl1());
			poManager.setPoEvent(poEvent);
			//设定设备类型
			String deviceType = poBaseDictService.remark2DeviceType(poRemoteDataVO.getWtfl1());
			poManager.setDeviceType(deviceType);
		}
		//来源设置 根据民意平台数据的来文分类 对应本系统中的数据
		if(StringUtils.isNotBlank(poRemoteDataVO.getLwflName())){
			Integer poEventSource = poBaseDictService.eventSourceValue2Key(poRemoteDataVO.getLwflName());
			poManager.setEventSource(poEventSource);
		}
		poManager.setSourcePlatform(1);
		/*if(StringUtils.isNotBlank(poRemoteDataVO.getTName())){
			//根据问题分类查询对应的编号 进行设置
			Integer poEvent=poBaseDictService.commonValue2Key(poRemoteDataVO.getTName(),PO_EVENT);
			poManager.setPoEvent(poEvent);
		}*/
		//当事人姓名和电话
		poManager.setContact(poRemoteDataVO.getDsrxm());
		//当事人电话
		poManager.setContactPhone(poRemoteDataVO.getDsrdh());
		if(StringUtils.isNotBlank(poRemoteDataVO.getOrgName())&&"科技管理处".equals(poRemoteDataVO.getOrgName())){
			poManager.setDutyContact("wangh");
			poManager.setRepairStatus(6);
		}
		//根据辖区和故障类型设置责任人 先判断为配时的情况
		if(StringUtils.isNotBlank(area)&&2==poManager.getPoType()){
			if(("10002".equals(area)||"10003".equals(area))&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
				poManager.setDutyContact("gd0002");
				poManager.setRepairStatus(6);
			}
			if("10001".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
				poManager.setDutyContact("hx0014");
				poManager.setRepairStatus(6);
			}
		}else if(StringUtils.isNotBlank(area)&&1==poManager.getPoType()){
			if("10001".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
				poManager.setRepairStatus(6);
				poManager.setDutyContact("bsy025");
			}
			if("10002".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
				poManager.setRepairStatus(6);
				poManager.setDutyContact("sl005");
			}
			if("10003".equals(area)&&(null!=poManager.getAreaId()&&!remoteCity.contains(poManager.getAreaId()))){
				poManager.setRepairStatus(6);
				poManager.setDutyContact("hx0003");
			}
		}

		if(StringUtils.isBlank(poManager.getDutyContact())){
			poManager.setRepairStatus(6);
			poManager.setDutyContact("wangh");
		}


	}

	public static void close(ResultSet rs, Statement stmt, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error("数据连接关闭失败！", e);
		}
	}

	@Override
	public void repeatMark(PoManager poManager) {
		if(null==poManager.getId()){
			throw  new BusinessException("传参id为空！");
		}
		if(StringUtils.isBlank(poManager.getPoCode())){
			//清空舆情编号
			poManagerDao.clearRepeatMark(poManager.getId());
			return;
		}
		//通过舆情编号搜索是否存在此舆情
		Long repeatId=poManagerDao.getByCode(poManager.getPoCode());
		if(null==repeatId){
			throw  new BusinessException("此舆情编号不存在！");
		}
		if(poManager.getId().equals(repeatId)){
			throw  new BusinessException("请勿标记自身为重复数据，请填写其他舆情编号！");
		}
		//判断这个舆情编号是不是一个重复舆情
		Integer repeatMark=poManagerDao.getRepeatMark(repeatId);
		if(null!=repeatMark){
			throw  new BusinessException("您不能关联一个重复的舆情编号！");
		}
		//查询原数据 做一次数据同步
		PoManager oldPoManager = poManagerDao.selectByPrimaryKey(repeatId);
		//修改为结案状态 并标记为重复
		poManager.setPoCode(null);
		poManager.setRepeatMark(repeatId);
		poManager.setPointId(oldPoManager.getPointId());
		poManager.setAreaId(oldPoManager.getAreaId());
		poManager.setSolutionTime(StringUtils.isNotBlank(oldPoManager.getSolutionTime())?oldPoManager.getSolutionTime():null);
		poManager.setSolution(oldPoManager.getSolution());
		poManager.setPoEvent(oldPoManager.getPoEvent());
		poManager.setConfirmerDescription(oldPoManager.getConfirmerDescription());
		poManager.setClosingPersonDescription(oldPoManager.getClosingPersonDescription());
		poManager.setRemark(oldPoManager.getRemark());
		poManager.setReason(oldPoManager.getReason());
		poManager.setFaultPhenomenon(oldPoManager.getFaultPhenomenon());
		//同步图片
		poManager.setBeforeAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
		poManager.setAfterAdjunctId(UUID.randomUUID().toString().replaceAll("-",""));
		//查询原舆情维修前图片
		if(StringUtils.isNotBlank(oldPoManager.getBeforeAdjunctId())){
			List<BaseVfs> beforeVfs = baseVfsDao.getByAdjunctId(oldPoManager.getBeforeAdjunctId());
			if(!StringHelper.isEmpty(beforeVfs)){
				for(BaseVfs baseVfs : beforeVfs){
					baseVfs.setAdjunctId(poManager.getBeforeAdjunctId());
					baseVfsDao.insertSelective(baseVfs);
				}
			}
		}
		//查询原舆情维修后图片
		if(StringUtils.isNotBlank(oldPoManager.getAfterAdjunctId())){
			List<BaseVfs> beforeVfs = baseVfsDao.getByAdjunctId(oldPoManager.getAfterAdjunctId());
			if(!StringHelper.isEmpty(beforeVfs)){
				for(BaseVfs baseVfs : beforeVfs){
					baseVfs.setAdjunctId(poManager.getAfterAdjunctId());
					baseVfsDao.insertSelective(baseVfs);
				}
			}
		}
		//已结案状态
		poManager.setRepairStatus(5);
		poManagerDao.updateByPrimaryKeySelective(poManager);
	}

	@Override
	public void batchUpdate(PoManagerRepeatMarkVO vo,UserSession userSession) {
		if(null==vo.getRepairStatus()){
			throw new BusinessException("维修状态为空！");
		}
		if(null!=vo.getIds()&&vo.getIds().size()>0){
			PoManager poManager = new PoManager();
			if(4==vo.getRepairStatus()&&StringUtils.isNotBlank(vo.getConfirmerDescription())){
				poManager.setConfirmerDescription(vo.getConfirmerDescription());
			}
			if(5==vo.getRepairStatus()&&StringUtils.isNotBlank(vo.getClosingPersonDescription())){
				poManager.setClosingPersonDescription(vo.getClosingPersonDescription());
			}
			poManager.setRepairStatus(vo.getRepairStatus());
			for(Long id : vo.getIds()){
				PoManager oldPoManager= poManagerDao.selectByPrimaryKey(id);
				poManager.setId(id);
				poManagerDao.updateByPrimaryKeySelective(poManager);
				saveLog(poManager,oldPoManager,1,userSession);
			}

		}
	}

	/**
	 * 获取原因占比
	 * @param appTicketCharReqVO
	 * @return
	 */
	@Override
	public AppTicketChartVO getReason(AppTicketCharReqVO appTicketCharReqVO) {
		AppTicketChartVO appTicketChartVO = new AppTicketChartVO();
		List<Map<String, Object>> map = poManagerDao.getReason(appTicketCharReqVO);
		String[] color = new String[]{"#6C74FD", "#35CBCC", "#4DCA72", "#FBD63A", "#F5647F", "#F4A244", "#33A0FF", "#CB80F0", "#5055D4",
				"#444F8B", "#6D75FE", "#36CBCB", "#4DCB73", "#FBD438"};
		for (int i = 0; i < map.size(); i++) {
			map.get(i).put("color", color[i]);
		}
		appTicketChartVO.setSeries(map);
		return appTicketChartVO;
	}

	/**
	 * 舆情表格统计
	 * @param appTicketCharReqVO
	 * @return
	 */
	@Override
	public List<PoManagerDataVO> getPoTable(AppTicketCharReqVO appTicketCharReqVO) {
		List<PoManagerDataVO> poManagerDataVOS = poManagerDao.getPoTable(appTicketCharReqVO);
		return poManagerDataVOS;
	}

	@Override
	public Integer getCount(AppTicketCharReqVO appTicketCharReqVO) {
		return poManagerDao.getCount(appTicketCharReqVO);
	}

	@Override
	public Integer getMainCityCount(AppTicketCharReqVO appTicketCharReqVO) {
		return poManagerDao.getMainCityCount(appTicketCharReqVO);
	}

	@Override
	public Integer getRemoteCityCount(AppTicketCharReqVO appTicketCharReqVO) {
		return poManagerDao.getRemoteCityCount(appTicketCharReqVO);
	}
}
