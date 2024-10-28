package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.BusinessException;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.*;
import com.zans.mms.model.*;
import com.zans.mms.service.IBaseOrgService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IDevicePointService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.asset.subset.AssetExportVO;
import com.zans.mms.vo.devicepoint.*;
import com.zans.mms.vo.devicepoint.check.DevicePointCheckReqVO;
import com.zans.mms.vo.devicepoint.map.DevicePointForMapResVO;
import com.zans.mms.vo.devicepoint.map.DevicePointMapQueryVO;
import com.zans.mms.vo.po.PoManagerResVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.zans.mms.config.MMSConstants.MSG_FORMAT_GIS;

/**
 *  DevicePointServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("devicePointService")
public class DevicePointServiceImpl extends BaseServiceImpl<DevicePoint> implements IDevicePointService {

	@Value("${api.upload.folder}")
	String uploadFolder;


	@Autowired
	private DevicePointMapper devicePointMapper;

	@Autowired
	WriteErrorMsgUtil writeErrorMsgUtil;

	@Resource
	public void setDevicePointMapper(DevicePointMapper mapper) {
		super.setBaseMapper(mapper);
		this.devicePointMapper = mapper;
	}

	@Autowired
	private DevicePointSubsetDetailMapper devicePointSubsetDetailMapper;

	@Autowired
	private AssetMapper assetMapper;

	@Autowired
	IFileService fileService;

	@Autowired
	IConstantItemService constantItemService;

	@Autowired
	private BaseAreaMapper baseAreaMapper;

	@Autowired
	private IBaseOrgService baseOrgService;

	@Autowired
	private BaseDeviceTypeMapper baseDeviceTypeMapper;

	@Autowired
	ExcelExportUtil excelExportUtil;

	@Resource
	DevicePointCheckLogMapper devicePointCheckLogMapper;

	@Resource
	SpeedDao speedDao;


	@Override
	public ApiResult getList(DevicePointQueryVO vo) {
		if (null != vo.getSubsetId() && vo.getSubsetId().intValue()!=0){
//			List<Long> pointIds = devicePointSubsetDetailMapper.getPointIdsBySubsetId(vo.getSubsetId());
//			vo.setPointIds(pointIds);
			int pageNum = vo.getPageNum();
			int pageSize = vo.getPageSize();
			Page page = PageHelper.startPage(pageNum, pageSize);
			//做建设单位特殊处理
			List<DevicePointResVO> result = devicePointSubsetDetailMapper.getPointList(vo);
			return ApiResult.success(new PageResult<DevicePointResVO>(page.getTotal(), result, pageSize, pageNum));

		}

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

		List<DevicePointResVO> result = devicePointMapper.getList(vo);
		return ApiResult.success(new PageResult<DevicePointResVO>(page.getTotal(), result, pageSize, pageNum));
	}


	/**
	 * 导入数据列表
	 * @param vo
	 * @return
	 */
	@Override
	public ApiResult getImportList(DevicePointQueryVO vo) {
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
		List<DevicePointResVO> result = devicePointMapper.getImportList(vo);
		return ApiResult.success(new PageResult<DevicePointResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public DevicePointDetailResVO getViewById(Long id) {
		DevicePointDetailResVO resVo = devicePointMapper.getViewById(id);
		AssetQueryVO assetQueryVO = new AssetQueryVO();
		assetQueryVO.setDeviceType(resVo.getDeviceType());
		assetQueryVO.setPointId(id);
		List<AssetResVO> list = assetMapper.getList(assetQueryVO);
		resVo.setAssetList(list);
		return resVo;
	}

	@Override
	public Boolean existRelation(Long id) {
		List<Asset> list =  assetMapper.getListByCondition(new HashMap<String,Object>(){{
			put("pointId",id);
		}});
		return !list.isEmpty() && list.size()>0;
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

	@Override
	public int deleteByUniqueId(Long id) {
		//先删除点位子集中的关联数据
		devicePointSubsetDetailMapper.deleteByPointId(id);
		return devicePointMapper.deleteByUniqueId(id);
	}

	@Override
	public Integer getIdByUniqueId(String pointId) {
		return devicePointMapper.getIdByUniqueId(pointId);
	}

	private ExcelSheetReader getDevicePointReader() {

		//point_code,point_code_change,point_name,area_name,road_type,device_type,power_way,network_linkway,longitude,latitude,map_source
		return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
				.notNullFields(Lists.newArrayList("point_code,point_name,area_name,device_type,longitude,latitude"))
				.headerClass(ExcelDevicePointVO.class).build();
	}

	/**
	 * 批量处理点位数据
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param userSession 用户数据
 	 * @return
	 */
	@Override
	public List<ExcelDevicePointVO> batchAddDevicePoint(String filePath, String fileName, UserSession userSession) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			throw new BusinessException("文件为空!");
		}

		String absoluteNewFilePath = this.uploadFolder + filePath;
		log.info("file#{}", file.getAbsolutePath());
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getDevicePointReader());
			if (linkResult == null) {
				throw new BusinessException("未填写任何点位信息!");
			}
			List<ExcelDevicePointVO> list = linkResult.getEntity().convertToRawTable(ExcelDevicePointVO.class);
			if (list == null || list.size() == 0) {
				throw new BusinessException("未填写任何点位!!");
			}

			if (!linkResult.getValid()) {
				throw new BusinessException("文件校验失败#");
			}
			return list;
		} catch (Exception ex) {
			log.error("读取用户上传文件失败#" + absoluteNewFilePath, ex);
			throw new BusinessException("用户上传模板错误或点位编号未填写，解析失败！");
		}
	}

	@Async
	@Override
	public void dealUploadData( Integer operation,Speed speed, List<ExcelDevicePointVO> excelDevicePointVOList, String absoluteNewFilePath, UserSession userSession) {
		Map<String, Object> map = this.dealExcelDevicePoint(operation,speed,excelDevicePointVOList, userSession,absoluteNewFilePath);
	}

	@Override
	public void updateDevicePoint(DevicePoint devicePoint) {
		//先查询修改前的数据
		DevicePoint oldDevicePoint = devicePointMapper.getOne(devicePoint.getId());
		//判断是否修改了经纬度 如果有修改经纬度 插入点位校验表
		try {
			if(oldDevicePoint.getLongitude().compareTo(devicePoint.getLongitude())!=0||
					oldDevicePoint.getLatitude().compareTo(devicePoint.getLatitude())!=0){
				DevicePointCheckReqVO devicePointCheckLog = new DevicePointCheckReqVO();
				devicePointCheckLog.setPointId(devicePoint.getId());
				devicePointCheckLog.setPrevLongitude(oldDevicePoint.getLongitude().toString());
				devicePointCheckLog.setPrevLatitude(oldDevicePoint.getLatitude().toString());
				devicePointCheckLog.setCreator(devicePoint.getCreator());
				devicePointCheckLog.setLongitude(devicePoint.getLongitude().toString());
				devicePointCheckLog.setLatitude(devicePoint.getLatitude().toString());
				devicePointCheckLogMapper.insertOne(devicePointCheckLog);
			}
		}catch (Exception e){
			log.error("修改了经纬度，但是日志记录失败了！");
		}
		devicePointMapper.updateDevicePoint(devicePoint);
	}

    @Override
    public int insertDevicePoint(DevicePointAddReqVO devicePointAddReqVO) {
		devicePointAddReqVO.setGis(String.format(MSG_FORMAT_GIS,devicePointAddReqVO.getLongitude(),devicePointAddReqVO.getLatitude()));
		devicePointAddReqVO.setPointType(2);
		return devicePointMapper.saveDevicePoint(devicePointAddReqVO);
    }

    /**
	 * 点位excel数据处理
	 * @param operation 操作 1：新增 2：修改 3：新增和修改
	 * @param list
	 * @param userSession
	 * @param absoluteNewFilePath
	 * @return
	 */
	public Map<String, Object> dealExcelDevicePoint(Integer operation,Speed speed,List<ExcelDevicePointVO> list,
							  UserSession userSession,String absoluteNewFilePath) {
		Map<String, Object> map = new HashMap<>();
		//定义失败计数
		int failCount = speed.getFailCount();
		int successCount = speed.getSuccessCount();
		String userName=userSession.getUserName();
		for (ExcelDevicePointVO vo : list) {
			failCount = speed.getFailCount();
			successCount = speed.getSuccessCount();
			if(vo.toString().contains("[必填]")){
				failCount++;
				successCount++;
				speed.setFailCount(failCount);
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"请补充该行点位编号!",absoluteNewFilePath);
				continue;
			}
			vo.setCreator(userName);
			vo.setImportId(speed.getId());
			//数据处理逻辑 如果pointCodeChange存在时进行什么操作
			//数据改变 如辖区name改为辖区id 经度和纬度合成gis point_type赋默认值1
			//辖区查询 如果辖区对应的areaId不存在 则给出错误提示
			if(StringUtils.isNotBlank(vo.getAreaName())){
				String areaId = baseAreaMapper.getAreaIdByName(vo.getAreaName());
				//如果辖区id为空 直接在excel中写入错误提示
				if (StringUtils.isBlank(areaId)) {
					failCount++;
					successCount++;
					speed.setFailCount(failCount);
					speed.setSuccessCount(successCount);
					speedDao.updateByPrimaryKeySelective(speed);
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "辖区名称为" + vo.getAreaName() + "的辖区id不存在!", absoluteNewFilePath);
					continue;
				}
				vo.setAreaId(baseAreaMapper.getAreaIdByName(vo.getAreaName()));
			}
			if(StringUtils.isNotBlank(vo.getOrgId())){
				String orgId=baseOrgService.getOrgIdByOrgName(vo.getOrgId());
				if(StringUtils.isBlank(orgId)){
					failCount++;
					successCount++;
					speed.setFailCount(failCount);
					speed.setSuccessCount(successCount);
					speedDao.updateByPrimaryKeySelective(speed);
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"负责单位名称为"+vo.getOrgId()+"的组织id不存在，请检查机构管理中的数据!",absoluteNewFilePath);
					continue;
				}
				vo.setOrgId(orgId);
			}
			if(StringUtils.isNotBlank(vo.getDeviceType())){
				String deviceType=baseDeviceTypeMapper.getTypeIdByName(vo.getDeviceType());
				//如果设备类型对应的id不存在  则写出错误提示信息
				if(StringUtils.isBlank(deviceType)){
					failCount++;
					successCount++;
					speed.setFailCount(failCount);
					speed.setSuccessCount(successCount);
					speedDao.updateByPrimaryKeySelective(speed);
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"设备类型为"+vo.getDeviceType()+"对应的id不存在!",absoluteNewFilePath);
					continue;
				}
				vo.setDeviceType(deviceType);
			}
			if(StringUtils.isNotBlank(vo.getLongitude())&&vo.getLongitude().contains("°")){
				failCount++;
				successCount++;
				speed.setFailCount(failCount);
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"该行经度格式错误，应为纯数字，不包含°等符号！",absoluteNewFilePath);
				continue;
			}
			if(StringUtils.isNotBlank(vo.getLatitude())&&vo.getLatitude().contains("°")){
				failCount++;
				successCount++;
				speed.setFailCount(failCount);
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"该行纬度格式错误，应为纯数字，不包含°等符号！",absoluteNewFilePath);
				continue;
			}

			//判断数据库中是否存在当前的pointCode及pointCodeChange
			Boolean isExistPointCode = devicePointMapper.getByCode(vo.getPointCode())>0;
			Boolean isExistPointCodeChange =false;
			//如果pointCodeChange不为空的情况
			if(!StringUtils.isBlank(vo.getPointCodeChange())){
				isExistPointCodeChange=devicePointMapper.getByCode(vo.getPointCodeChange())>0;
			}
			dealData(operation,speed,isExistPointCode,isExistPointCodeChange,absoluteNewFilePath,vo);
		}
		return map;
	}

	/**
	 * 点位数据处理逻辑
	 * @param operation 操作类型 1：新增 2：修改 3：新增和修改
	 * @param isExistPointCode 点位编码是否存在
	 * @param isExistPointCodeChange 修改后的点位编码是否存在
	 * @param absoluteNewFilePath 文件绝对路径
	 * @param vo 点位数据
	 * @return
	 */
	private void dealData(Integer operation,Speed speed,Boolean isExistPointCode, Boolean isExistPointCodeChange, String absoluteNewFilePath, ExcelDevicePointVO vo) {
		//当前条数
		int successCount = speed.getSuccessCount();
		//失败条数
		int failCount = speed.getFailCount();
		//定义新增计数
		int addCount = speed.getAddCount();
		//定义修改计数
		int updateCount = speed.getUpdateCount();
		//分情况判断 情况1 pointCode和pointCodeChange都为空，做新增
		if(!isExistPointCode&&!isExistPointCodeChange){
			if(!StringUtils.isBlank(vo.getPointCodeChange())){
				vo.setPointCode(vo.getPointCodeChange());
			}
			try {
				vo.setIsTest(1);
				vo.setPointType(1);
				//默认将地图来源设为高德
				vo.setMapSource("2");
				//在此校验 点位名称 辖区名称 设备类型 经度 纬度  负责单位字段
				if(StringUtils.isNotBlank(vo.getPointName())&&StringUtils.isNotBlank(vo.getAreaId())
						&&StringUtils.isNotBlank(vo.getDeviceType())&&StringUtils.isNotBlank(vo.getLatitude())
						&&StringUtils.isNotBlank(vo.getLongitude())&&StringUtils.isNotBlank(vo.getOrgId())) {
					if(operation==2){
						writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行点位编号，您选择导入方式为数据更新，但在点位列表中不存在点位编号为"+vo.getPointCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
					}else {
						devicePointMapper.insertDevicePoint(vo);
					}
					addCount++;
					speed.setAddCount(addCount);

				}else {
					failCount++;
					speed.setFailCount(failCount);
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"请检查该行点位名称、辖区名称、设备类型、经度、纬度、负责单位等字段是否填写完整！",absoluteNewFilePath);
				}

			}catch (Exception e){
				failCount++;
				speed.setFailCount(failCount);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据新增失败！",absoluteNewFilePath);
			}finally {
				successCount++;
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
			}

		}
		//pointCode不存在，但是pointCodeChange存在的情况，根据pointCodeChange做修改
		else if(!isExistPointCode&&isExistPointCodeChange){
			//10-28添加 根据pointCodeChange先去查询出这条点位数据
			if(operation==1){
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行点位编号，您选择导入方式为数据新增，但在点位列表中已存在点位编号为"+vo.getPointCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
			}else {
				vo.setPointCode(vo.getPointCodeChange());
				ExcelDevicePointVO excelDevicePointVO = devicePointMapper.getOneByCode(vo.getPointCode());
				excelDevicePointVO.setImportId(speed.getId());
				devicePointMapper.insertDevicePoint(excelDevicePointVO);
				devicePointMapper.updateByPointCode(vo);
			}
			updateCount++;
			speed.setUpdateCount(updateCount);
			successCount++;
			speed.setSuccessCount(successCount);
			speedDao.updateByPrimaryKeySelective(speed);

		}
		//pointCode存在，但是pointCodeChange不存在的情况，根据pointCode做修改
		else if(isExistPointCode&&!isExistPointCodeChange){
			if(StringUtils.isBlank(vo.getPointCodeChange())){
				vo.setPointCodeChange(vo.getPointCode());
			}
			try {
				if(operation==1){
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行点位编号，您选择导入方式为数据新增，但在点位列表中已存在点位编号为"+vo.getPointCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
				}else{
					vo.setPointCode(vo.getPointCodeChange());
					ExcelDevicePointVO excelDevicePointVO = devicePointMapper.getOneByCode(vo.getPointCode());
					excelDevicePointVO.setImportId(speed.getId());
					devicePointMapper.insertDevicePoint(excelDevicePointVO);
					devicePointMapper.updateByPointCodeChange(vo);
				}
				updateCount++;

				speed.setUpdateCount(updateCount);

			}catch (Exception e){
				log.error("错误信息#{}",e);
				failCount++;
				speed.setFailCount(failCount);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);

			}finally {
				successCount++;
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
			}

		}//pointCode和pointCodeChange都存在的请况
		else{
			try {
				if(operation==1){
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行点位编号，您选择导入方式为数据新增，但在点位列表中已存在点位编号为"+vo.getPointCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
				}else {


				//首先删除pointCode的记录，然后修改pointCodeChange的记录
				devicePointMapper.deleteByPointCode(vo.getPointCode());
				//修改pointCodeChange的数据
				vo.setPointCode(vo.getPointCodeChange());
				ExcelDevicePointVO excelDevicePointVO = devicePointMapper.getOneByCode(vo.getPointCode());
				excelDevicePointVO.setImportId(speed.getId());
				devicePointMapper.insertDevicePoint(excelDevicePointVO);
				devicePointMapper.updateByPointCode(vo);
				}
				updateCount++;
				speed.setUpdateCount(updateCount);

			}catch (Exception e){
				failCount++;
				speed.setFailCount(failCount);
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);
			}finally {
				successCount++;
				speed.setSuccessCount(successCount);
				speedDao.updateByPrimaryKeySelective(speed);
			}

		}

	}

	@Override
	public ApiResult getMapList(DevicePointMapQueryVO vo) {
		JSONObject jsonObject = new JSONObject();
		List<DevicePointForMapResVO> pointList = devicePointMapper.getListForMap(vo);
		if (pointList != null && pointList.size() > 0) {
			int i = 0;
			for (DevicePointForMapResVO resVO : pointList) {
				//设置序号
				resVO.setId(i++);
			}
		}
		//根据设备类型统计
		Map<String, Long> deviceTypeCountMap = pointList.stream().collect(Collectors.groupingBy(DevicePointForMapResVO::getDType, Collectors.counting()));
		jsonObject.put("statsDeviceType", deviceTypeCountMap);
		jsonObject.put("total", pointList.size());
		Map<String, Object> result = MapBuilder.getBuilder()
				.put("pointList", pointList)
				.put("statistical", jsonObject)
				.build();
		return ApiResult.success(result);

	}

	/**
	 * 点位数据导出
	 * @param devicePointQueryVO
	 * @param fileName 导出文件名称
	 * @return
	 */
	@Override
	public String export(DevicePointExportQueryVO devicePointQueryVO, String fileName) {
		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList,"点位编码","点位名称","辖区名称","路口类型","设备类型","取电方式","通讯接入方式","经度","维度","负责单位");

		//此时写完文件头
		String absoluteNewFilePath= null;
		try {
			absoluteNewFilePath=excelExportUtil.writeHeader("点位列表",rowNameList,fileName);
		}catch (Exception e){
			log.error("点位数据导出时，写文件头错误{}",e);
		}
		List<DevicePointExportVO> devicePointList= devicePointMapper.getExportList(devicePointQueryVO);
		this.writePointData(absoluteNewFilePath,devicePointList);
		return absoluteNewFilePath;
	}


	/**
	 * 写点位数据
	 * @param absoluteNewFilePath 绝对路径
	 * @param devicePointList 点位数据列表
	 */
	private void writePointData(String absoluteNewFilePath, List<DevicePointExportVO> devicePointList) {
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
				//row.createCell(currentCell++).setCellValue(devicePointList.get(i).getId());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPointCode());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPointName());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getAreaName());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getRoadType());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getDeviceType());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getPowerWay());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getNetworkLinkway());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getLongitude());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getLatitude());
				row.createCell(currentCell++).setCellValue(devicePointList.get(i).getOrgName());
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


	@Override
	public ApiResult getTrafficSignalList(PoManagerResVO vo) {
		if(StringUtils.isBlank(vo.getPointName())){
			vo.setPointNameList(new ArrayList<>());
		}else{
			String[] split = vo.getPointName().split(" ");
			List<String> pointNameList= new ArrayList<>();
			for(int i =0;i<split.length;i++){
				if(StringUtils.isNotBlank(split[i])){
					pointNameList.add(split[i]);
				}
			}
			vo.setPointNameList(pointNameList);
		}
		return ApiResult.success(devicePointMapper.getTrafficSignalList(vo));
	}

	@Override
	public List<String> getBuildCompany() {
		return devicePointMapper.getBuildCompany();
	}


	@Override
	public void backupTable() {
		String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
		String backupTableName = "device_point_"+date+"_bak";
		devicePointMapper.backupTable(backupTableName);
		devicePointMapper.synchronousData(backupTableName);
	}

	@Override
	public void updateImportDevicePoint(DevicePoint devicePoint) {
		devicePointMapper.updateImportDevicePoint(devicePoint);
	}


	/**
	 * 导入确认按钮
	 * @param queryVO
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void importConfig(DevicePointQueryVO queryVO,UserSession userSession) {
		//查询出本次的所有点位信息
		List<ExcelDevicePointVO> excelDevicePointVOList = devicePointMapper.getByImportId(queryVO.getImportId());
		if(!StringHelper.isEmpty(excelDevicePointVOList)){
			for(ExcelDevicePointVO excelDevicePointVO : excelDevicePointVOList){
				excelDevicePointVO.setCreator(userSession.getUserName());
				if(excelDevicePointVO.getOperation()==1){
					//插入
					devicePointMapper.insertOne(excelDevicePointVO);
				}
				if(excelDevicePointVO.getOperation()==2){
					//修改
					devicePointMapper.updateExcelDevicePoint(excelDevicePointVO);
				}
			}
		}

	}
}
