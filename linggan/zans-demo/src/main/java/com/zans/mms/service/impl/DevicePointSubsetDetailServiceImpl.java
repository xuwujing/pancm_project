package com.zans.mms.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.PatrolConstants;
import com.zans.mms.dao.mms.DevicePointMapper;
import com.zans.mms.dao.mms.DevicePointSubsetDetailMapper;
import com.zans.mms.dao.mms.DevicePointSubsetMapper;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.model.DevicePointSubset;
import com.zans.mms.model.DevicePointSubsetDetail;
import com.zans.mms.service.IDevicePointSubsetDetailService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.subset.AssetSubsetDetaiQueryReqVO;
import com.zans.mms.vo.devicepoint.DevicePointResVO;
import com.zans.mms.vo.devicepoint.subset.DevicePointSubsetExportVO;
import com.zans.mms.vo.devicepoint.subset.ExcelDevicePointSubsetDetailVO;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddByConditionReqVO;
import com.zans.mms.vo.devicepoint.subset.PointSubsetDetailAddReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *  DevicePointSubsetDetailServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("devicePointSubsetDetailService")
public class DevicePointSubsetDetailServiceImpl extends BaseServiceImpl<DevicePointSubsetDetail> implements IDevicePointSubsetDetailService {


	@Autowired
	private DevicePointSubsetDetailMapper devicePointSubsetDetailMapper;

	@Autowired
	private DevicePointMapper devicePointMapper;

	@Value("${api.upload.folder}")
	String uploadFolder;

	@Autowired
	IFileService fileService;

	@Autowired
	DevicePointSubsetMapper devicePointSubsetMapper;

	@Autowired
	WriteErrorMsgUtil writeErrorMsgUtil;

	@Autowired
	ExcelExportUtil excelExportUtil;

	@Resource
	public void setDevicePointSubsetDetailMapper(DevicePointSubsetDetailMapper mapper) {
		super.setBaseMapper(mapper);
		this.devicePointSubsetDetailMapper = mapper;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void groupsAddPoint(PointSubsetDetailAddReqVO reqVO, UserSession userSession) {
		Long subsetId = reqVO.getSubsetId();
		Set<Long> pointIdSet = reqVO.getPointIds();
		List<Long> oldPointList = devicePointSubsetDetailMapper.getPointIdBySubsetId(null);
		HashSet<Long> oldPointSet = new HashSet<>(oldPointList);
		//1强行添加点位(移除了其他子集中的点位，并添加到自己子集)2忽略已存在的
		Integer addType = reqVO.getAddType();

		dealWithPointSubset(userSession, subsetId, pointIdSet, oldPointSet, addType);
	}

	public void dealWithPointSubset(UserSession userSession, Long subsetId, Set<Long> pointIdSet, Set<Long> oldPointSet, Integer addType) {
		//1强行添加点位(移除了其他子集中的点位，并添加到自己子集)2忽略已存在的
		if (PatrolConstants.POINT_SUBSET_ADD_FORCE.equals(addType)) {
			Set<Long> retainPointSet = new HashSet<>();
			retainPointSet.addAll(pointIdSet);
			//得到交集
			retainPointSet.retainAll(oldPointSet);
			if (retainPointSet != null && retainPointSet.size()>0){
				devicePointSubsetDetailMapper.deleteByPointIds(retainPointSet);
			}
			batchInsertPointSubsetDetail(userSession, subsetId, pointIdSet);
			return;
		}

		if (PatrolConstants.POINT_SUBSET_ADD_IGNORE.equals(addType)) {
			//2忽略已存在的
			//得到去重后的结果
			pointIdSet.removeAll(oldPointSet);
			if (pointIdSet != null && pointIdSet.size() > 0) {
				batchInsertPointSubsetDetail(userSession, subsetId, pointIdSet);
			}
		}
	}

	public void batchInsertPointSubsetDetail(UserSession userSession, Long subsetId, Set<Long> pointIdSet) {
		DevicePointSubsetDetail subsetDetail;
		for (Long pointId : pointIdSet) {
			subsetDetail = new DevicePointSubsetDetail();
			subsetDetail.setPointId(pointId);
			subsetDetail.setSubsetId(subsetId);
			subsetDetail.setCreator(userSession.getUserName());
			subsetDetail.setCreateTime(new Date());
			subsetDetail.setUpdateTime(new Date());
			devicePointSubsetDetailMapper.insertSelective(subsetDetail);
		}
	}

	/**
	 * 根据条件添加点位
	 * @param reqVO
	 * @param userSession
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void groupsAddPointByCondition(PointSubsetDetailAddByConditionReqVO reqVO, UserSession userSession) {
		Long subsetId = reqVO.getSubsetId();
		Set<Long> pointIdList=new HashSet<>();
		//1强行添加点位(移除了其他子集中的点位，并添加到自己子集)2忽略已存在的
		Integer addType = reqVO.getAddType();
		//直接把reqVo作为查询条件 查询所有符合条件的点位信息
		List<Long> pointIds = devicePointMapper.getPointIdByCondition(reqVO);
		//去重 查询当前分组id中的所有pointId 然后去掉这部分
		List<Long> oldPointIds = devicePointSubsetDetailMapper.getPointIdBySubsetId(null);

		Set<Long> pointIdSet = new HashSet<>(pointIds);
		Set<Long> oldPointIdSet = new HashSet<>(oldPointIds);

		dealWithPointSubset(userSession, subsetId, pointIdSet, oldPointIdSet, addType);


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

	/**
	 * 批量添加点位子集列表数据
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param userSession
	 * @return
	 */
	@Override
	public ApiResult batchAddDevicePointSubset(String filePath, String fileName, UserSession userSession,String json) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			return null;
		}
		String absoluteNewFilePath = this.uploadFolder + filePath;
		log.info("file#{}", file.getAbsolutePath());
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getDevicePointSubsetReader());


			if (linkResult == null) {
				return ApiResult.error("未填写任何点位子集信息!");
			}
			List<ExcelDevicePointSubsetDetailVO> list = linkResult.getEntity().convertToRawTable(ExcelDevicePointSubsetDetailVO.class);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何子集点位!!");
			}
			log.info("link.size#{}", list.size());
			Map<String, Object> map = this.dealExcelDeviceSubsetDetailPoint(list, userSession, json,absoluteNewFilePath);

			if (!linkResult.getValid()) {
				log.error("文件校验失败#" + absoluteNewFilePath);
				//return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
				map.put("errorPath",absoluteNewFilePath);
				map.put("msg","数据格式部分存在错误！");
			}
			return ApiResult.success(map);
		} catch (Exception ex) {
			log.error("读取用户上传文件失败#" + absoluteNewFilePath, ex);
			return ApiResult.error(5010,"用户上传模板错误，解析失败！");
		}
	}

	/**
	 * 巡检子集数据导出方法
	 * @return
	 */
	@Override
	public String exportFile(String filName) {

		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList,"子集名称","辖区","点位类型","点位编号","点位名称","经度","纬度");
		//此时写完文件头
		String absoluteNewFilePath=null;
		try {
			absoluteNewFilePath=excelExportUtil.writeHeader("巡检子集列表",rowNameList,filName);
		}catch (Exception e){
			log.error("巡检子集数据导出时，写文件头错误{}",e);
		}
		//再查询所有的巡检子集导出信息
		List<DevicePointSubsetExportVO> devicePointSubsetExportVOList=devicePointSubsetDetailMapper.selectExportData();
		writeData(absoluteNewFilePath,devicePointSubsetExportVOList);
		//开始写数据
		return absoluteNewFilePath;
	}

	/**
	 * 巡检子集点位导出方法
	 * @param subsetId 子集id
	 * @param fileName 文件名称
	 * @return
	 */
	@Override
	public String export(String subsetId, String fileName) {
		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList,"id","点位编号","点位名称","辖区名称","路口类型","设备类型","取电方式/位置","通讯接入方式","经度","纬度","所属单位及设备类型");
		//此时写完文件头
		String absoluteNewFilePath=null;
		try {
			absoluteNewFilePath=excelExportUtil.writeHeader("巡检子集列表",rowNameList,fileName);
		}catch (Exception e){
			log.error("点位数据导出时，写文件头错误{}",e);
		}
		//根据子集id 查询所有数据
		List<DevicePoint> devicePointList = devicePointSubsetDetailMapper.getExportDataBySubsetId(subsetId);
		this.writePointData(absoluteNewFilePath,devicePointList);
		return absoluteNewFilePath;
	}

	/**
	 * 写点位数据
	 * @param absoluteNewFilePath 绝对路径
	 * @param devicePointList 点位数据列表
	 */
	private void writePointData(String absoluteNewFilePath, List<DevicePoint> devicePointList) {
		FileInputStream fs=null;
		FileOutputStream out =null;
		try{
			//获取absoluteNewFilePath
			fs=new FileInputStream(absoluteNewFilePath);
			//做2003和2007兼容
			Workbook wb = WorkbookFactory.create(fs);

			//默认取第一个sheet页
			Sheet sheetAt = wb.getSheetAt(0);

			//做数据遍历 进行数据写入、
			//逻辑处理 需修改
			for(int i =0;i<devicePointList.size();i++){
				Row row = sheetAt.createRow(i+1);
				int currentCell=0;
//"id","点位编号","点位名称","辖区名称","路口类型","设备类型","取电方式/位置","通讯接入方式","经度","纬度","所属单位及设备类型");
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPointCode());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPointName());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getAreaId());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getRoadType());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getDeviceType());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPowerWay());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getNetworkLinkway());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getLongitude().toString());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getLatitude().toString());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getAffiliatedUnits());

			}
			out = new FileOutputStream(absoluteNewFilePath);
			wb.write(out);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
			log.error("导出数据写入出现错误！");
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					log.error("输出流关闭出现异常{}",e);
				}
			}
			if(fs!=null){
				try {
					fs.close();
				} catch (IOException e) {
					log.error("文件输入流出现异常{}",e);
				}
			}
		}
	}


	/**
	 * excel中数据写入
	 * @param absoluteNewFilePath 文件绝对路径
	 * @param devicePointSubsetExportVOList 数据列表
	 */
	public void writeData(String absoluteNewFilePath, List<DevicePointSubsetExportVO> devicePointSubsetExportVOList) {
		FileInputStream fs=null;
		FileOutputStream out =null;
		try{
			//获取absoluteNewFilePath
			fs=new FileInputStream(absoluteNewFilePath);
			//做2003和2007兼容
			Workbook wb = WorkbookFactory.create(fs);

			//默认取第一个sheet页
			Sheet sheetAt = wb.getSheetAt(0);

			//做数据遍历 进行数据写入、
			//逻辑处理 需修改
			for(int i =0;i<devicePointSubsetExportVOList.size();i++){
				Row row = sheetAt.createRow(i+1);
				int currentCell=0;
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getSubsetName());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getAreaName());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getDeviceType());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getPointCode());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getPointName());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getLongitude());
				row.createCell(currentCell++).setCellValue(devicePointSubsetExportVOList.get(i).getLatitude());
			}
			out = new FileOutputStream(absoluteNewFilePath);
			wb.write(out);
			out.flush();
		}catch (Exception e){
			e.printStackTrace();
			log.error("导出数据写入出现错误！");
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					log.error("输出流关闭出现异常{}",e);
				}
			}
			if(fs!=null){
				try {
					fs.close();
				} catch (IOException e) {
					log.error("文件输入流出现异常{}",e);
				}
			}
		}
	}

	/**
	 * excel巡检子集数据处理
	 * @param list 数据
	 * @param userSession 用户信息
	 * @param json 参数 isClean isCreate
	 * @param absoluteNewFilePath 绝对路径
	 * @return
	 */
	private Map<String, Object> dealExcelDeviceSubsetDetailPoint(List<ExcelDevicePointSubsetDetailVO> list, UserSession userSession,String json,String absoluteNewFilePath) {
		String nickname=userSession.getNickName();
		//判断是否需要删除历史数据 以及是否新增不存在的点位子集
		JSONObject jsonObject = JSONObject.parseObject(json);
		boolean isDelete =  Boolean.parseBoolean(jsonObject.get("isClean").toString());
		boolean isCreate = Boolean.parseBoolean(jsonObject.get("isCreate").toString());
		//1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的  addType
		Integer addType = jsonObject.getInteger ("addType");
		if (null == addType || addType.intValue() == 0 ){
			addType = PatrolConstants.POINT_SUBSET_ADD_IGNORE;
		}

		//装返回的数据
		Map<String, Object> map =new HashMap<>();
		//定义失败的数据条数
		int failCount=0;
		//先对数据做一次处理 比如不能导入重复数据 因此要对数据进行去重，要知道哪些巡检子集名字存在，哪些巡检子集不存在 需要新增的
		//先对子集名称进行一次去重
		List<String> subsetNameList = new ArrayList<>();
		for (ExcelDevicePointSubsetDetailVO vo : list) {
			subsetNameList.add(vo.getSubsetName());
		}
		Set s = new HashSet(subsetNameList);
		subsetNameList.clear();
		//得到去重后的子集名称
		subsetNameList.addAll(s);
		//如果需要删除历史数据
		if(isDelete){
			deleteDevicePointSubsetDetail(subsetNameList);
		}
		//如果需要新增子集
		if(isCreate){
			addDevicePointSubset(subsetNameList,nickname);
		}
		//正常新增
		for (ExcelDevicePointSubsetDetailVO vo : list) {
			if(vo.toString().contains("[必填]")){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"请补充该行必填项！",absoluteNewFilePath);
				continue;
			}
///			failCount = dealData(vo, absoluteNewFilePath, failCount, nickname);
			if (PatrolConstants.POINT_SUBSET_ADD_IGNORE.equals(addType)) {
				////1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的  addType
				failCount = dealDataIgnoer(vo, absoluteNewFilePath, failCount, nickname);
			} else if (PatrolConstants.POINT_SUBSET_ADD_FORCE.equals(addType)) {
				failCount = dealDataForce(vo, absoluteNewFilePath, failCount, nickname);
			}

		}
		map.put("failCount",failCount);
		map.put("successCount",list.size()-failCount);
		if(list.size()!=list.size()-failCount){
			map.put("errorPath",absoluteNewFilePath);
		}
		return map;

	}

	private int dealDataForce(ExcelDevicePointSubsetDetailVO vo, String absoluteNewFilePath, int failCount, String nickname) {
		DevicePointSubsetDetail devicePointSubsetDetail = new DevicePointSubsetDetail();
		String subsetId = devicePointSubsetMapper.getIdBySubsetName(vo.getSubsetName());
		if(StringUtils.isNotBlank(subsetId)) {
			devicePointSubsetDetail.setSubsetId(Long.parseLong(subsetId));
			Long pointId=devicePointMapper.getIdByCode(vo.getPointCode());
			if(null != pointId){
				devicePointSubsetDetail.setPointId(pointId);
				//先判断这条数据是否存在 如果不存在 则新增 存在则强行添加点位(移除了其他子集中的点位，并添加到自己子集）
				int countByPointId = devicePointSubsetDetailMapper.getCountByPointId (pointId);
				if(countByPointId>0){
					devicePointSubsetDetailMapper.deleteByPointId(pointId);
				}
				devicePointSubsetDetail.setCreator(nickname);
				devicePointSubsetDetail.setCreateTime(new Date());
				devicePointSubsetDetail.setUpdateTime(new Date());
				devicePointSubsetDetailMapper.insert(devicePointSubsetDetail);
			}else {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"点位编号为"+vo.getPointCode()+"的数据不存在！",absoluteNewFilePath);
			}
		}else{
			failCount++;
			writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"巡检子集名称为"+vo.getSubsetName()+"的数据不存在，无法插入！",absoluteNewFilePath);
		}
		return failCount;

	}

	private int dealDataIgnoer(ExcelDevicePointSubsetDetailVO vo, String absoluteNewFilePath, int failCount, String nickname) {
		DevicePointSubsetDetail devicePointSubsetDetail = new DevicePointSubsetDetail();
		String subsetId = devicePointSubsetMapper.getIdBySubsetName(vo.getSubsetName());
		if(StringUtils.isNotBlank(subsetId)) {
			devicePointSubsetDetail.setSubsetId(Long.parseLong(subsetId));
			Long pointId=devicePointMapper.getIdByCode(vo.getPointCode());
			if(null != pointId){
				devicePointSubsetDetail.setPointId(pointId);
				//先判断这条数据是否存在 如果不存在 则新增 存在则不新增跳过
				int countByPointId = devicePointSubsetDetailMapper.getCountByPointId (pointId);
				if(countByPointId>0){
					return failCount;

				}else{
					devicePointSubsetDetail.setCreator(nickname);
					devicePointSubsetDetail.setCreateTime(new Date());
					devicePointSubsetDetail.setUpdateTime(new Date());
					//正常做插入
					devicePointSubsetDetailMapper.insert(devicePointSubsetDetail);
				}
			}else {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"点位编号为"+vo.getPointCode()+"的数据不存在！",absoluteNewFilePath);
			}
		}else{
			failCount++;
			writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"巡检子集名称为"+vo.getSubsetName()+"的数据不存在，无法插入！",absoluteNewFilePath);
		}
		return failCount;

	}

	/**
	 * 巡检子集数据处理
	 * @param vo 数据
	 * @param absoluteNewFilePath 文件绝对路径
	 * @param failCount 失败条数
	 * @param nickname 用户昵称
	 * @return
	 */
	private int dealData(ExcelDevicePointSubsetDetailVO vo, String absoluteNewFilePath, int failCount, String nickname) {
		DevicePointSubsetDetail devicePointSubsetDetail = new DevicePointSubsetDetail();
		String subsetId = devicePointSubsetMapper.getIdBySubsetName(vo.getSubsetName());
		if(StringUtils.isNotBlank(subsetId)) {
			devicePointSubsetDetail.setSubsetId(Long.parseLong(subsetId));
			Long pointId=devicePointMapper.getIdByCode(vo.getPointCode());
			if(null != pointId){
				devicePointSubsetDetail.setPointId(pointId);
				//先判断这条数据是否存在 如果不存在 则新增 存在则不新增
				String id = devicePointSubsetDetailMapper.selectBySubsetIdAndPointId(devicePointSubsetDetail);
				if(StringUtils.isBlank(id)){
					devicePointSubsetDetail.setCreator(nickname);
					devicePointSubsetDetail.setCreateTime(new Date());
					devicePointSubsetDetail.setUpdateTime(new Date());
					//正常做插入
					devicePointSubsetDetailMapper.insert(devicePointSubsetDetail);
				}else{
					failCount++;
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"本条数据在数据库中已存在!",absoluteNewFilePath);
				}
			}else {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"点位编号为"+vo.getPointCode()+"的数据不存在！",absoluteNewFilePath);
			}
		}else{
			failCount++;
			writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"巡检子集名称为"+vo.getSubsetName()+"的数据不存在，无法插入！",absoluteNewFilePath);
		}
		return failCount;
	}

	/**
	 * 删除点位子集详情中的数据方法
	 * @param subsetNameList 子集名称集合 通过名称查询子集id删除
	 */
	private void deleteDevicePointSubsetDetail(List<String> subsetNameList) {
		//遍历查询每个子集id subsetId 如果存在则删除，如果不存在则不做操作
		for (String subsetName:subsetNameList) {
			String subsetId=devicePointSubsetMapper.getIdBySubsetName(subsetName);
			//如果非空 做删除
			if(StringUtils.isNotBlank(subsetId)){
				devicePointSubsetDetailMapper.deleteDetailBySubsetId(Long.parseLong(subsetId));
			}
		}
	}


	/**
	 * 新增巡检子集数据
	 * @param subsetNameList 巡检子集名称
	 * @param nickname 用户昵称 用于做数据插入的creator
	 */
	private void addDevicePointSubset(List<String> subsetNameList,String nickname) {
		//遍历查询每个子集id subsetId 如果存在则删除，如果不存在则不做操作
		for (String subsetName:subsetNameList) {
			if(subsetName.contains("必填")){
				continue;
			}
			String subsetId=devicePointSubsetMapper.getIdBySubsetName(subsetName);
			//如果根据子集名称查询出来的id为空 则进行新增
			if(StringUtils.isBlank(subsetId)){
				DevicePointSubset devicePointSubset = new DevicePointSubset();
				devicePointSubset.setSubsetName(subsetName);
				devicePointSubset.setCreator(nickname);
				devicePointSubset.setCreateTime(new Date());
				devicePointSubset.setUpdateTime(new Date());
				//数据插入
				devicePointSubsetMapper.insert(devicePointSubset);
			}
		}
	}

	private ExcelSheetReader getDevicePointSubsetReader() {
		return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
				.notNullFields(Lists.newArrayList("subset_name,point_code"))
				.headerClass(ExcelDevicePointSubsetDetailVO.class).build();
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public void removePoint(PointSubsetDetailAddReqVO reqVO) {
		for (Long pointId : reqVO.getPointIds()) {
			int count = devicePointSubsetDetailMapper.removeAsset(reqVO.getSubsetId(),pointId);
		}
	}

	@Override
	public ApiResult groupList(AssetSubsetDetaiQueryReqVO reqVO) {
		int pageNum = reqVO.getPageNum();
		int pageSize = reqVO.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<DevicePointResVO> result = devicePointSubsetDetailMapper.groupList(reqVO);

		return ApiResult.success(new PageResult<DevicePointResVO>(page.getTotal(), result,  pageSize, pageNum));
	}

	/**
	 * 数据去重
	 * @param list1
	 * @param list2
	 * @return
	 */
	public Set<Long> removeSame(List<Long> list1,List<Long> list2){
		Set<Long> assetList=new HashSet<>();
		HashSet hs1 = new HashSet(list1);
		HashSet hs2 = new HashSet(list2);
		hs1.removeAll(hs2);
		assetList.addAll(hs1);
		return assetList;
	}
}
