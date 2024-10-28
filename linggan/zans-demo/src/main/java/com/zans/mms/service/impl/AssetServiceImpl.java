package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.AssetConstants;
import com.zans.mms.dao.guard.AssetDiagnosticThresholdDao;
import com.zans.mms.dao.guard.AssetMapper;
import com.zans.mms.dao.mms.AssetSubsetDetailMapper;
import com.zans.mms.dao.mms.BaseDeviceTypeMapper;
import com.zans.mms.dao.mms.DevicePointMapper;
import com.zans.mms.model.Asset;
import com.zans.mms.service.IAreaService;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.*;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisHisRespVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoExRespVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosisInfoRespVO;
import com.zans.mms.vo.asset.diagnosis.AssetDiagnosticThresholdVO;
import com.zans.mms.vo.radius.QzViewRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 *  AssetServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("assetService")
public class AssetServiceImpl extends BaseServiceImpl<Asset> implements IAssetService {


	@Autowired
	private AssetMapper assetMapper;

	@Value("${api.upload.folder}")
	String uploadFolder;

	@Autowired
	IFileService fileService;

	@Autowired
	DevicePointMapper devicePointMapper;

	@Autowired
	BaseDeviceTypeMapper baseDeviceTypeMapper;

	@Autowired
	AssetSubsetDetailMapper assetSubsetDetailMapper;

	@Autowired
	WriteErrorMsgUtil writeErrorMsgUtil;

	@Autowired
	IAreaService areaService;

	@Resource
	private AssetDiagnosticThresholdDao assetDiagnosticThresholdDao;

	@Resource
	public void setAssetMapper(AssetMapper mapper) {
		super.setBaseMapper(mapper);
		this.assetMapper = mapper;
	}



	@Override
	public ApiResult getList(AssetQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<AssetResVO> result = assetMapper.getList(vo);
		return ApiResult.success(new PageResult<AssetResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public ApiResult getMonitorList(AssetQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<AssetMonitorResVO> result = assetMapper.getMonitorList(vo);
		return ApiResult.success(new PageResult<AssetMonitorResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public AssetViewResVO getViewById(Long id) {
		return assetMapper.getViewById(id);
	}

	@Override
	public Integer getIdByUniqueId(String assetId) {
		return assetMapper.getIdByUniqueId(assetId);
	}

	@Override
	public Boolean existRelation(String id) {
		return assetSubsetDetailMapper.getByAssetId(id)>0;
	}

	@Override
	public int deleteByUniqueId(String id) {
		assetSubsetDetailMapper.deleteDetailByAssetId(id);
		return assetMapper.deleteByUniqueId(id);
	}


	//预留方法 处理数据请求 如处理ip存在 但是账号或密码为空的情况
	public Map<String, Object> check(List<ExcelAssetVO> list){
		Map<String, Object> map = new HashMap<>();
		//无法插入的条数
		int count=0;
		//无法插入的assetCode列表
		List<String> assetCodeList = new ArrayList<>();
		for (ExcelAssetVO vo:list) {
			//如果ip不为空
			if(!StringUtils.isBlank(vo.getNetworkIp())){
				//判断账户和密码是否为空  如果存在为空的 不进行插入 并且记录他的pointCode
				Boolean flag=StringUtils.isNotBlank(vo.getDeviceAccount())&&StringUtils.isNotBlank(vo.getDevicePwd());
				if(!flag){
					assetCodeList.add(vo.getAssetCode());
					count++;
				}
			}
		}
		map.put("failCount",count);
		map.put("failAssetCode",assetCodeList);
		return map;
	}

	/**
	 * 批量插入资产数据
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param userSession
	 * @return
	 */
	@Override
	public ApiResult batchAddAsset(String filePath, String fileName, UserSession userSession,String type) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			return ApiResult.error("文件为空!");
		}
		String absoluteNewFilePath = this.uploadFolder + filePath;
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getAssetReader(type));
			if (linkResult == null) {
				return ApiResult.error("未填写任何资产信息!");
			}
			List<ExcelAssetVO> list = linkResult.getEntity().convertToRawTable(ExcelAssetVO.class);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何资产信息!");
			}
			//处理资产数据的设备类型方法
			List<ExcelAssetVO> assetVOList=dealAssetDeviceType(list,type);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何资产!!");
			}
			log.info("link.size#{}", list.size());
			Map<String, Object> map = this.dealExcelAsset(assetVOList, userSession,absoluteNewFilePath);
			if (!linkResult.getValid()) {
				log.error("文件校验失败#" + absoluteNewFilePath);
				map.put("errorPath",absoluteNewFilePath);
				map.put("msg","部分数据存在格式错误");
			}

			return ApiResult.success(map);
		} catch (Exception ex) {
			log.error("用户上传模板错误或数据格式有误#" + absoluteNewFilePath, ex);
			return ApiResult.error(5010,"用户上传模板错误，解析失败！");
		}
	}

	/**
	 * 资产导入模板下载通用方法
	 * @param type
	 */
	@Override
	public Map<String, String> downloadTemplate(String type) {
		String date= DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
		String filePath=this.uploadFolder+"template/资产导入-电警-excel模板.xlsx";
		String fileCnName="资产电警导入模板" + date+".xlsx";
		switch (type){
			case "electricPolice":
				filePath = this.uploadFolder+"template/资产导入-电警-excel模板.xlsx";
				fileCnName = "资产电警导入模板" + date+".xlsx";
				break;
			case  "electronicLabel":
				filePath = this.uploadFolder+"template/资产导入-电子标识-excel模板.xlsx";
				fileCnName = "资产电子标识导入模板" + date+".xlsx";
				break;
			case   "monitor":
				filePath = this.uploadFolder+"template/资产导入-监控-excel模板.xlsx";
				fileCnName = "资产监控导入模板" + date+".xlsx";
				break;
			case  "bayonet":
				filePath = this.uploadFolder+"template/资产导入-卡口-excel模板.xlsx";
				fileCnName = "资产卡口导入模板" + date+".xlsx";
				break;
			case  "trafficSignal":
				filePath = this.uploadFolder+"template/资产导入-信号机-excel模板.xlsx";
				fileCnName = "资产信号机导入模板" + date+".xlsx";
				break;
			case   "inductionScreen":
				filePath = this.uploadFolder+"template/资产导入-诱导屏-excel模板.xlsx";
				fileCnName = "资产诱导屏导入模板" + date+".xlsx";
				break;
			default:
				break;
		}
		Map <String, String> map =  new HashMap<>();
		map.put("filePath",filePath);
		map.put("fileCnName",fileCnName);
		return map;
	}

	//资产数据处理方法  处理数据的设备类型
	private List<ExcelAssetVO> dealAssetDeviceType(List<ExcelAssetVO> list,String type) {
		//用于处理默认设备类型
		List<ExcelAssetVO> assetVOList=new ArrayList<>();
		//如果type为电警时
		String deviceType=null;
		if(AssetConstants.ELECTRIC_POLICE.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.ELECTRIC_POLICE_NAME);
		}else if(AssetConstants.ELECTRONIC_LABEL.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.ELECTRONIC_LABEL_NAME);
		}else if(AssetConstants.MONITOR.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.MONITOR_NAME);
		}else if(AssetConstants.BAYONET.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.BAYONET_NAME);
		}else if(AssetConstants.TRAFFIC_SIGNAL.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.TRAFFIC_SIGNAL_NAME);
		}else if(AssetConstants.INDUCTION_SCREEN.equals(type)){
			deviceType=baseDeviceTypeMapper.getTypeIdByName(AssetConstants.INDUCTION_SCREEN_NAME);
		}else {
			deviceType=AssetConstants.UNKNOW;
		}
		for (ExcelAssetVO vo:list) {
			vo.setDeviceType(deviceType);
			assetVOList.add(vo);
		}
		return assetVOList;
	}

	/**
	 * 处理资产excel数据
	 * @param list 从excel读取的数据集合
	 * @param userSession 用户信息
	 * @param absoluteNewFilePath 上传文件的路径
	 * @return
	 */
	private Map<String, Object> dealExcelAsset(List<ExcelAssetVO> list, UserSession userSession,String absoluteNewFilePath) {
		Map<String, Object> map = new HashMap<>();
		String nickname = userSession.getNickName();
		//定义失败计数
		int failCount=0;
		//先根据pointCode查询是否有对应的点位信息 如果没有 就不插入 如果有在做数据校验
		for (ExcelAssetVO vo : list) {
			//对有非空项不填的处理
			if(vo.toString().contains("[必填]")){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"请补充该行必填项！",absoluteNewFilePath);
				continue;
			}
			if(StringUtils.isBlank(vo.getMaintainStatus())){
				vo.setMaintainStatus("正常");
			}
			//是否测试数据标识
			vo.setIsTest(1);
			String pointCode=vo.getPointCode();
			Long pointId=devicePointMapper.getIdByCode(pointCode);
			//存在该pointCode的情况 且拿到了他的id 用于做关联
			if(null == pointId){
				//如果pointid为空，跳过此次循环，返回这条数据
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"点位编号"+pointCode+"对应的数据为空！请校验后输入！",absoluteNewFilePath);
				continue;
			}
			vo.setPointId(pointId.toString());
			vo.setUpdateTime(new Date());
			//判断asset_code和asset_code_change是否存在的情况 用于做数据修改或新增
			Boolean isExistAssetCode = assetMapper.getExistAssetCode(vo.getAssetCode())>0;
			Boolean isExistAssetCodeChange=false;
			//如果修改后的资产编码不为空  去查询有没有对应的数据
			if(!StringUtils.isBlank(vo.getAssetCodeChange())){
				isExistAssetCodeChange=assetMapper.getExistAssetCode(vo.getAssetCodeChange())>0;
			}
			//根据资产编码和修改后的资产编码进行方法调用
			failCount=dealData(isExistAssetCode,isExistAssetCodeChange,nickname,failCount,vo,absoluteNewFilePath);
			//根据两个code存在的情况 对数据进行维护
		}
		map.put("failCount",failCount);
		map.put("successCount",list.size()-failCount);
		if(list.size()!=list.size()-failCount){
			map.put("errorPath",absoluteNewFilePath);
		}
		return map;

	}

	//根据资产编码和修改后的资产编码进行数据处理
	private int dealData(Boolean isExistAssetCode, Boolean isExistAssetCodeChange,String nickname,Integer failCount,ExcelAssetVO vo,String absoluteNewFilePath) {
		//分情况判断 情况1 assetCode和assetCodeChange都为空，做新增
		if(!isExistAssetCode&&!isExistAssetCodeChange){
			vo.setCreateTime(new Date());
			vo.setCreator(nickname);
			if(!StringUtils.isBlank(vo.getAssetCodeChange())){
				vo.setAssetCode(vo.getAssetCodeChange());
			}
			try {
				assetMapper.insertAsset(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据插入失败",absoluteNewFilePath);
			}
		}//assetCode不存在，但是assetCodeChange存在的情况，根据pointCodeChange做修改
		else if(!isExistAssetCode&&isExistAssetCodeChange){
			try {
				vo.setAssetCode(vo.getAssetCodeChange());
				assetMapper.updateByAssetCode(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败",absoluteNewFilePath);
			}

		}
		//assetCode存在，但是assetCodeChange不存在的情况，根据pointCode做修改
		else if(isExistAssetCode&&!isExistAssetCodeChange){
			if(StringUtils.isBlank(vo.getAssetCodeChange())){
				vo.setAssetCodeChange(vo.getAssetCode());
			}
			try {
				assetMapper.updateByAssetCodeChange(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);
			}
		}
		//assetCode和assetCodeChange都存在的请况
		else{
			try {
				//首先删除assetCode的记录，然后修改assetCodeChange的记录
				assetMapper.deleteByAssetCode(vo.getAssetCode());
				//修改assetCodeChange的数据
				vo.setAssetCode(vo.getAssetCodeChange());
				assetMapper.updateByAssetCode(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);
			}
		}
		return failCount;
	}


	/**
	 * 数据读取
	 * @param type 类型：电警/点位/诱导屏等
	 * @return
	 */
	private ExcelSheetReader getAssetReader(String type) {
		//根据type类型判断和返回
		//当type是电警时
		if(AssetConstants.ELECTRIC_POLICE.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelElectricPoliceVO.class).build();
		}
		//type为电子标识时
		if(AssetConstants.ELECTRONIC_LABEL.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelElectronicIdentificationVO.class).build();
		}
		//type为监控时
		if(AssetConstants.MONITOR.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelMonitorAssetVO.class).build();
		}
		//type为卡口时
		if(AssetConstants.BAYONET.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelBayonetAssetVO.class).build();
		}
		//type为信号机时
		if(AssetConstants.TRAFFIC_SIGNAL.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelSignalAssetVO.class).build();
		}
		//type为诱导屏时
		if(AssetConstants.INDUCTION_SCREEN.equals(type)){
			return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
					.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
					.headerClass(ExcelGuidanceScreenAssetVO.class).build();
		}

		return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
				.notNullFields(Lists.newArrayList("asset_code,point_code,device_type"))
				.headerClass(ExcelAssetVO.class).build();
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
	public ApiResult chooseAssetList(AssetQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<AssetResVO> result = assetMapper.chooseAssetList(vo);
		return ApiResult.success(new PageResult<AssetResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public List<Asset> getListByCondition(HashMap<String, Object> map) {
		return assetMapper.getListByCondition(map);
	}

    @Override
    public ApiResult getDiagnosisView(String ip,String traceId) {
		AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO = new AssetDiagnosisInfoRespVO();
		//如果traceId不为空，就表明是查询历史数据
		if(StringUtils.isNotEmpty(traceId)){
			assetDiagnosisInfoRespVO = assetMapper.getDiagnosisHisView(ip,traceId);
		}else {
			assetDiagnosisInfoRespVO = assetMapper.getDiagnosisView(ip);
		}

		AssetDiagnosticThresholdVO assetDiagnosticThresholdVO = new AssetDiagnosticThresholdVO();
		assetDiagnosticThresholdVO.setDeviceId(assetDiagnosisInfoRespVO.getAssetCode());
		assetDiagnosticThresholdVO.setIpaddress(assetDiagnosisInfoRespVO.getIpAddr());
		List<AssetDiagnosticThresholdVO> assetDiagnosticThresholdVOS = assetDiagnosticThresholdDao.queryAll(assetDiagnosticThresholdVO);
		for (AssetDiagnosisInfoExRespVO assetDiagnosisInfoExVO : assetDiagnosisInfoRespVO.getAssetDiagnosisInfoExVOS()) {
			int faultType = assetDiagnosisInfoExVO.getFaultType();
			int curFaultTypeThreshold = assetDiagnosisInfoExVO.getFaultTypeResult();
			boolean flag = isCompare(faultType,curFaultTypeThreshold,assetDiagnosticThresholdVOS);
			if (flag){
				assetDiagnosisInfoExVO.setFaultTypeResultName("异常");
			}else {
				assetDiagnosisInfoExVO.setFaultTypeResultName("正常");
			}
		}

		//查询三天内的
		List<AssetDiagnosisHisRespVO> hisRespVOS = assetMapper.getThreeDaysDiagnosisHisList(ip);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("diagnosis_info", assetDiagnosisInfoRespVO);
		resultMap.put("diagnosis_his", hisRespVOS);
        return ApiResult.success(resultMap);
    }


	private boolean isCompare(int faultType, int curFaultTypeThreshold, List<AssetDiagnosticThresholdVO> assetDiagnosticThresholdVOS){
		for (AssetDiagnosticThresholdVO vo : assetDiagnosticThresholdVOS) {
			int faultType2 = vo.getFaultType();
			Integer faultTypeThreshold = vo.getFaultTypeThreshold();
			if( faultType2 == faultType){
				return curFaultTypeThreshold<=faultTypeThreshold;
			}
		}
		return false;
	}
	/**
	 * @param ip
	 * @return com.zans.portal.vo.radius.QzViewRespVO
	 * @Author pancm
	 * @Description 根据IP查询
	 * @Date 2020/10/29
	 * @Param [ip]
	 */
	@Override
	public QzViewRespVO findByIp(String ip) {
		/*Asset asset = assetMapper.findByIpAddr(ip);
		if (asset == null) {
			return null;
		}
		QzViewRespVO qzViewRespVO = new QzViewRespVO();
		if (asset.getAreaId() != null) {
			qzViewRespVO.setAreaName(areaService.getAreaNameById(asset.getAreaId()));
		}
		if (asset.getDeviceType() != null) {
			qzViewRespVO.setTypeName(deviceTypeService.getNameByType(asset.getDeviceType()));
		}
		qzViewRespVO.setPointName(asset.getPointName());
		qzViewRespVO.setProjectName(asset.getProjectName());
		qzViewRespVO.setContractorPerson(asset.getContractorPerson());
		qzViewRespVO.setContractorPhone(asset.getContractorPhone());
		qzViewRespVO.setDeviceModelDes(asset.getDeviceModelDes());
		qzViewRespVO.setDeviceModelBrand(asset.getDeviceModelBrand());
		qzViewRespVO.setMaintainCompany(asset.getMaintainCompany());
		qzViewRespVO.setIpAddr(asset.getIpAddr());
		return qzViewRespVO;*/
		return null;
	}

	@Override
	public List<SwitcherMacInterfaceResVO> getSwitchMacInterface() {
		List<SwitcherMacInterfaceResVO> list = assetMapper.getSwitchMacInterface();
		List<SwitcherMacInterfaceResVO> resList = new ArrayList<>();
		for (SwitcherMacInterfaceResVO vo : list) {
			String nasPortId = vo.getNasPortId();
			String id = vo.getNasPortId();
			if (!org.springframework.util.StringUtils.isEmpty(nasPortId)) {
				nasPortId = nasPortId.replace("3D", "");
				nasPortId = nasPortId.replace("=3B", ";");
				nasPortId= StringHelper.getPortId(nasPortId);
				id= nasPortId.substring(4);
				vo.setNasPortId(nasPortId);
			}
			vo.setInterfaceDetail("GigabitEthernet"+nasPortId);
			vo.setPId(id);
//			if (vo.getAccessPolicy().intValue()==2 && vo.getAlive().intValue()==1){
//				resList.add(vo);
//				continue;
//			}
//			if (vo.getAccessPolicy().intValue()==1 && vo.getAliveQz().intValue()==1){
//				resList.add(vo);
//			}
			resList.add(vo);
		}

		return resList;
	}

	@Override
	public SwitcherMacInterfaceResVO getSwitchMacInterfaceByPort(String portId) {
		return assetMapper.getSwitchMacInterfaceByPort(portId);
	}

	@Override
	public SwitcherMacInterfaceResVO getSwitchMacInterfaceByPortDown(String portId) {
		return assetMapper.getSwitchMacInterfaceByPortDown(portId);
	}

	@Override
    public void resetStatusByMac(String mac) {
		assetMapper.deleteByMac(mac);
    }
}

