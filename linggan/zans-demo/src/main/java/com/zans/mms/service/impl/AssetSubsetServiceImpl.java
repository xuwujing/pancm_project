package com.zans.mms.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.AssetConstants;
import com.zans.mms.dao.guard.AssetMapper;
import com.zans.mms.dao.mms.AssetSubsetDetailMapper;
import com.zans.mms.dao.mms.AssetSubsetMapper;
import com.zans.mms.dao.mms.BaseDeviceTypeMapper;
import com.zans.mms.model.AssetSubset;
import com.zans.mms.model.AssetSubsetDetail;
import com.zans.mms.model.AssetSubsetStats;
import com.zans.mms.service.IAssetSubsetService;
import com.zans.mms.service.IAssetSubsetStatsService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.AssetForMapResVO;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.asset.subset.*;
import com.zans.mms.vo.devicepoint.DevicePointQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  AssetSubsetServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("assetSubsetService")
public class AssetSubsetServiceImpl extends BaseServiceImpl<AssetSubset> implements IAssetSubsetService {

	@Autowired
	private AssetSubsetMapper assetSubsetMapper;

	@Autowired
	IFileService fileService;

	@Autowired
	WriteErrorMsgUtil writeErrorMsgUtil;

	@Value("${api.upload.folder}")
	String uploadFolder;

	@Resource
	public void setAssetSubsetMapper(AssetSubsetMapper mapper) {
		super.setBaseMapper(mapper);
		this.assetSubsetMapper = mapper;
	}

	@Autowired
	AssetSubsetDetailMapper assetSubsetDetailMapper;

	@Autowired
	private BaseDeviceTypeMapper baseDeviceTypeMapper;

	@Autowired
	private IAssetSubsetStatsService assetSubsetStatsService;

	@Resource
	AssetMapper assetMapper;

	private String getSubsetStatsTime(){
		Date now = new Date();
		Date todayEightClock = DateHelper.parseDatetime(DateHelper.formatDate(now, DateHelper.yyyy_MM_dd + AssetConstants.ASSET_START_TIME_SUFFIX));
		if (todayEightClock.getTime()>System.currentTimeMillis()) {
			//取昨天的八点
			Date yesterday = DateHelper.plusDays(now, -1);
			return DateHelper.formatDate(yesterday,DateHelper.yyyy_MM_dd) ;
		}
		return DateHelper.formatDate(now,DateHelper.yyyy_MM_dd) ;
	}

	@Override
	public ApiResult getList(AssetSubsetQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<AssetSubsetResVO> result = assetSubsetMapper.getList(vo);

		String statsDate = getSubsetStatsTime();
		String startTime = DateHelper.jointDate(statsDate, AssetConstants.ASSET_START_TIME_SUFFIX);
		String endTime = DateHelper.jointDate(statsDate, AssetConstants.ASSET_END_TIME_SUFFIX);

		for (AssetSubsetResVO resVO : result) {
			AssetSubsetStats assetSubsetStats = assetSubsetStatsService.getLastRecordBySubsetId(resVO.getSubsetId());
			if (null == assetSubsetStats){
				continue;
			}
			resVO.setFaultCount(assetSubsetStats.getFaultNumber());
			resVO.setNormalCount(assetSubsetStats.getNormalNumber());
			resVO.setStopCount(assetSubsetStats.getDisableNumber());
			resVO.setOnlineRate(assetSubsetStats.getOnlineRate().toString());
			resVO.setLastUpdateTime(DateHelper.getDateTime(assetSubsetStats.getStatsTime()) );
		}

		return ApiResult.success(new PageResult<AssetSubsetResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public Integer getIdByUniqueId(String subsetId) {
		return assetSubsetMapper.getIdByUniqueId(subsetId);
	}

	@Override
	public Boolean existRelation(Long id) {
		AssetSubsetDetaiQueryReqVO reqVO = new AssetSubsetDetaiQueryReqVO();
		reqVO.setSubsetId(id);
		List<AssetResVO> result = assetSubsetDetailMapper.groupList(reqVO);
		return !result.isEmpty() && result.size()>0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteByUniqueId(Long id) {
		assetSubsetDetailMapper.deleteDetailBySubsetId(id);
		return assetSubsetMapper.deleteByUniqueId(id);
	}

	@Override
	public int findByName(String subsetName, String subsetId) {
		return assetSubsetMapper.findByName(subsetName,subsetId);
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
	 * 资产子集数据批量处理
	 * @param filePath 文件路径
	 * @param fileName 文件名称
	 * @param userSession 用户信息
	 * @param json 前端参数
	 * @return
	 */
	@Override
	public ApiResult batchAddAssetSubset(String filePath, String fileName, UserSession userSession, String json) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			return null;
		}
		String absoluteNewFilePath = this.uploadFolder + filePath;
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getAssetSubsetReader());
			if (linkResult == null) {
				return ApiResult.error("未填写任何资产子集信息!");
			}
			List<ExcelAssetSubsetVO> list = linkResult.getEntity().convertToRawTable(ExcelAssetSubsetVO.class);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写资产子集!!");
			}
			log.info("link.size#{}", list.size());
			Map<String, Object> map = this.dealExcelAssetSubset(list, userSession, json,absoluteNewFilePath);
			//处理文件中必填项为填的情况  返回错误路径下载
			if (!linkResult.getValid()) {
				log.error("文件校验失败#" + absoluteNewFilePath);
				map.put("msg","部分数据格式存在错误");
				map.put("errorPath",absoluteNewFilePath);
			}

			return ApiResult.success(map);
		} catch (Exception ex) {
			log.error("读取用户上传文件失败#" + absoluteNewFilePath, ex);
			return ApiResult.error(5010,"用户上传模板错误，解析失败！");
		}
	}

	/**
	 * 查重当前设备类型和时间不重复的资产子集名称列表
	 * @param assetSubsetLineChartReqVO
	 * @return
	 */
	@Override
	public List<String> getSubsetNameList(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO) {
		//首先定义横坐标时间
		List<String> subsetNameList = new ArrayList<>();
		subsetNameList.add("时间");
		List<String> result=assetSubsetMapper.getSubsetNameList(assetSubsetLineChartReqVO);
		subsetNameList.addAll(result);
		return subsetNameList;
	}

	/**
	 * 获取在线率数据列表
	 * @param assetSubsetLineChartReqVO 请求参数  设备类型 选择的时间
	 * @return
	 */
	@Override
	public ApiResult getLineChartData(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO) {
		//查询当天的数据
		List<AssetSubsetLineChartVO> assetSubsetLineChartVOS=assetSubsetMapper.getLineChartData(assetSubsetLineChartReqVO);
		log.info("assetSubsetLineChartVOS的size{}",assetSubsetLineChartVOS.size());
		//查询不重复的子集名称
		List<String> subsetNameList = assetSubsetMapper.getSubsetNameList(assetSubsetLineChartReqVO);
		//做数据处理
		JSONArray jsonArray = new JSONArray();
		//算出循环的次数
		int forCount=assetSubsetLineChartVOS.size()/subsetNameList.size();
		String[] time={"8:00","10:00","12:00","14:00","16:00","18:00","20:00","22:00","00:00","2:00","4:00","6:00"};
		for(int i =0 ;i<forCount;i++){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("时间",time[i]);
			//在线率数据添加方法
			addOnlineRate(jsonObject,i,subsetNameList,assetSubsetLineChartVOS);
			jsonArray.add(jsonObject);
		}
		return ApiResult.success(jsonArray);
	}

	/**
	 * 资产子集在线率
	 * @param assetSubsetLineChartReqVO
	 * @return
	 */
	@Override
	public ApiResult getAssetSubsetChartData(AssetSubsetLineChartReqVO assetSubsetLineChartReqVO) {
		String title=null;
		//初始化的情况 没有subset_id  设置标题和subsetId
		if(null ==assetSubsetLineChartReqVO.getSubsetId()){
			AssetSubsetOnlineRateVO assetSubsetOnlineRateVO= assetSubsetMapper.getAssetSubsetOnlineData().get(0);
			assetSubsetLineChartReqVO.setSubsetId(assetSubsetOnlineRateVO.getSubsetId());
			//设置标题
			title=assetSubsetOnlineRateVO.getSubsetName()+"在线率";
		}else{
			//根据subsetId查询标题
			String subsetName=assetSubsetMapper.getSubsetNameById(assetSubsetLineChartReqVO.getSubsetId());
			title=subsetName+"在线率";
		}
		//通过资产子集id和选择的时间去查询数据 如果时间不传 默认查询最近24小时数据 如果传了时间 查询当天24小时数据
		List<AssetSubsetOnlineRateVO> AssetSubsetOnlineRateVOList=null;
		//横坐标列表
		List<String> xAxis = new ArrayList<>();
		List<String> onlineRateList= new ArrayList<>();
		String pattern=null;
		//如果需要显示24小时内数据的情况
		if(!assetSubsetLineChartReqVO.getIsHistory()) {
			AssetSubsetOnlineRateVOList = assetSubsetMapper.getOnlineRateList(assetSubsetLineChartReqVO.getSubsetId());
			//根据查询出来的数据 设置横坐标和值
			pattern="HH:mm";
		}else{
			//查询历史数据的情况
			AssetSubsetOnlineRateVOList = assetSubsetMapper.getHistoryOnlineRateListBySubsetId(assetSubsetLineChartReqVO);
			pattern="MM-dd";
		}
		for (AssetSubsetOnlineRateVO vo:AssetSubsetOnlineRateVOList) {
			onlineRateList.add(vo.getOnlineRate());
			xAxis.add(new SimpleDateFormat(pattern).format(vo.getStatsTime()));
		}
		//数据展示列表
		Map<String, Object> map = new HashMap<>();
		map.put("yAxis",onlineRateList);
		map.put("xAxis",xAxis);
		map.put("title",title);
		return ApiResult.success(map);
	}

	/**
	 * 资产子集实时在线率展示接口 显示最后一条在线率数据
	 * @return
	 */
	@Override
	public ApiResult getAssetSubsetOnlineData() {
		List<AssetSubsetOnlineRateVO> assetSubsetOnlineRateVOList = assetSubsetMapper.getAssetSubsetOnlineData();
		return ApiResult.success(assetSubsetOnlineRateVOList);
	}


	/**
	 * 添加在线率数据
	 * @param jsonObject 一条展示的数据对象
	 * @param i 第i条数据
	 * @param subsetNameList 不重复的名称列表
	 * @param assetSubsetLineChartVOS 在线率列表
	 */
	private void addOnlineRate(JSONObject jsonObject, int i,List<String> subsetNameList,List<AssetSubsetLineChartVO> assetSubsetLineChartVOS) {
		for(int j=0;j<subsetNameList.size();j++){
			jsonObject.put(subsetNameList.get(j),assetSubsetLineChartVOS.get(i).getOnlineRate()==null?0:assetSubsetLineChartVOS.get(i).getOnlineRate());
			i=i+assetSubsetLineChartVOS.size()/subsetNameList.size();
		}
	}

	/**
	 * 处理资产子集数据
	 * @param list
	 * @param userSession
	 * @param json
	 * @param absoluteNewFilePath
	 * @return
	 */
	private  Map<String, Object>  dealExcelAssetSubset(List<ExcelAssetSubsetVO> list, UserSession userSession,String json,String absoluteNewFilePath) {
		String nickname=userSession.getNickName();
		//判断是否需要删除历史数据 以及是否新增不存在的资产子集
		JSONObject jsonObject = JSONObject.parseObject(json);
		boolean isDelete =  Boolean.parseBoolean(jsonObject.get("isClean").toString());
		boolean isCreate =  Boolean.parseBoolean(jsonObject.get("isCreate").toString());
		//装返回的数据
		Map<String, Object> map =new HashMap<>();
		//定义失败的数据条数
		int failCount=0;
		//先对数据做一次处理 比如不能导入重复数据 因此要对数据进行去重，要知道哪些资产子集名字存在，哪些资产子集不存在 需要新增的
		// 首先对子集名称进行一次去重
		List<Map<String, Object>> subsetList = new ArrayList<>();
		for (ExcelAssetSubsetVO vo : list) {
			Map<String, Object> m =new HashMap<>();
			m.put("subsetName",vo.getSubsetName());
			String deviceType=baseDeviceTypeMapper.getTypeIdByName(vo.getDeviceType());
			if(StringUtils.isBlank(deviceType)){
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"设备类型"+vo.getDeviceType()+"不存在",absoluteNewFilePath);
				continue;
			}
			m.put("deviceType",deviceType);
			subsetList.add(m);
		}
		Set s = new HashSet(subsetList);
		subsetList.clear();
		//得到去重后的子集名称
		subsetList.addAll(s);
		//如果需要删除历史数据
		if(isDelete){
			deleteAssetSubsetDetail(subsetList);
		}
		//是否需要新增
		if(isCreate){
			addAssetSubset(subsetList,nickname);
		}
		//数据处理逻辑方法
		failCount=dealData(list,failCount,absoluteNewFilePath,nickname);
		map.put("failCount",failCount);
		map.put("successCount",list.size()-failCount);
		if(list.size()!=list.size()-failCount){
			map.put("errorPath",absoluteNewFilePath);
		}
		return map;
	}

	/**
	 * 资产子集数据处理
	 * @param list 数据集合
	 * @param failCount 失败条数
	 * @param absoluteNewFilePath 文件路径
	 * @param nickname 用户昵称
	 * @return
	 */
	private int dealData(List<ExcelAssetSubsetVO> list, int failCount, String absoluteNewFilePath, String nickname) {
		for (ExcelAssetSubsetVO vo : list) {
			if (vo.toString().contains("[必填]")) {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请补充该行必填项！", absoluteNewFilePath);
				continue;
			}
			String deviceType = vo.getDeviceType();
			AssetSubsetDetail assetSubsetDetail = new AssetSubsetDetail();
			String deviceTypeId = baseDeviceTypeMapper.getTypeIdByName(vo.getDeviceType());
			//如果查询出来的设备类型为空
			if (StringUtils.isBlank(deviceTypeId)) {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "子集设备类型为" + deviceType + "没有对应的id!", absoluteNewFilePath);
				continue;
			}
			vo.setDeviceType(deviceTypeId);
			//判断资产子集名称是否存在
			Map<String, Object> m = new HashMap<>();
			m.put("subsetName", vo.getSubsetName());
			m.put("deviceType", vo.getDeviceType());
			String subsetId = assetSubsetMapper.getIdBySubsetNameAndDeviceType(m);
			//如果资产子集存在 把数据插入详情表中
			if (StringUtils.isNotBlank(subsetId)) {
				assetSubsetDetail.setSubsetId(Long.parseLong(subsetId));
				//查询资产表，查看对应的资产编码是否存在设备类型对应的id
				List<String> assetIdList = assetMapper.getIdByCode(vo.getAssetCode(), vo.getDeviceType());
				String assetId = null;
				if (assetIdList.size() != 0 && assetIdList != null) {
					assetId = assetIdList.get(0);
				}
				//如果资产id不为空
				if (StringUtils.isNotBlank(assetId)) {
					assetSubsetDetail.setAssetId(Long.parseLong(assetId));
					//通过assetId和subsetId查询数据是否存在 如果不存在才新增
					String id = assetSubsetDetailMapper.selectBySubsetIdAndAssetId(assetSubsetDetail.getSubsetId(), assetSubsetDetail.getAssetId());
					if (StringUtils.isBlank(id)) {
						assetSubsetDetail.setCreator(nickname);
						assetSubsetDetail.setCreateTime(new Date());
						assetSubsetDetail.setUpdateTime(new Date());
						//可以插入的情况
						assetSubsetDetailMapper.insert(assetSubsetDetail);
					} else {
						failCount++;
						writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "该行数据已在数据库中存在！", absoluteNewFilePath);
					}
				} else {
					failCount++;
					writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "设备编号为" + vo.getAssetCode() + "且设备类型为" + deviceType + "的数据不存在！请校验后再进行导入！", absoluteNewFilePath);
				}
			} else {
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "资产子集名称为" + vo.getSubsetName() + "且设备类型为" + deviceType + "的数据不存在，无法进行导入！", absoluteNewFilePath);
			}
		}
		return failCount;
	}

	/**
	 * 增加资产子集列表数据
	 * @param subsetNameList
	 * @param nickname
	 */
	private void addAssetSubset(List<Map<String, Object>> subsetNameList, String nickname) {
		//遍历集合 查询是否有不存在的数据
		for (Map<String, Object> map:subsetNameList) {
			if(map.toString().contains("必填")){
				continue;
			}
			//先判断名称是否存在
			String id = assetSubsetMapper.getIdBySubsetName(map.get("subsetName").toString());
			//如果不存在 才进行新增
			if (StringUtils.isBlank(id)) {
					AssetSubset assetSubset = new AssetSubset();
					assetSubset.setSubsetName(map.get("subsetName").toString());
					assetSubset.setCreator(nickname);
					assetSubset.setCreateTime(new Date());
					assetSubset.setUpdateTime(new Date());
					assetSubset.setDeviceType(map.get("deviceType").toString());
					assetSubsetMapper.insert(assetSubset);
				}
		}
	}

	/**
	 * 根据名称批量删除点位子集数据方法
	 * @param subsetNameList
	 */
	private void deleteAssetSubsetDetail(List<Map<String, Object>> subsetNameList) {
		//遍历查询每个子集id subsetId 如果存在则删除，如果不存在则不做操作
		for (Map<String, Object> map:subsetNameList) {
			String subsetId=assetSubsetMapper.getIdBySubsetNameAndDeviceType(map);
			//如果非空 做删除
			if(StringUtils.isNotBlank(subsetId)){
				assetSubsetDetailMapper.deleteDetailBySubsetId(Long.parseLong(subsetId));
			}
		}
	}



	private ExcelSheetReader getAssetSubsetReader() {
		return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
				.notNullFields(Lists.newArrayList("subset_name,device_type,asset_code"))
				.headerClass(ExcelAssetSubsetVO.class).build();
	}


	@Override
	public ApiResult getMapList(DevicePointQueryVO vo) {
		JSONObject jsonObject = new JSONObject();
		List<AssetForMapResVO> assetList;
		if(vo.getSubsetId() == null) {
			assetList = assetMapper.getListForMap();
		} else {
			assetList = assetSubsetDetailMapper.getListForMap(vo.getSubsetId());
		}
		if (assetList != null && assetList.size() > 0) {
			int i = 0;
			for (AssetForMapResVO resVO : assetList) {
				//设置序号
				resVO.setId(i++);
			}
		}
		//根据设备类型统计
		Map<String, Long> deviceTypeCountMap = assetList.stream().collect(Collectors.groupingBy(AssetForMapResVO::getDType, Collectors.counting()));
		jsonObject.put("statsDeviceType", deviceTypeCountMap);
		jsonObject.put("total", assetList.size());
		Map<String, Object> result = MapBuilder.getBuilder()
				.put("assetList", assetList)
				.put("statistical", jsonObject)
				.build();
		return ApiResult.success(result);
	}

	@Override
	public ApiResult getSelectList() {
		return ApiResult.success(assetSubsetMapper.getSelectList());
	}
}
