package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.office.excel.ExcelEntity;
import com.zans.base.office.excel.ExcelSheetReader;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.guard.AssetMapper;
import com.zans.mms.dao.mms.BaseAreaMapper;
import com.zans.mms.dao.mms.BaseDeviceTypeMapper;
import com.zans.mms.dao.mms.DevicePointMapper;
import com.zans.mms.dao.mms.DevicePointSubsetDetailMapper;
import com.zans.mms.model.Asset;
import com.zans.mms.model.DevicePoint;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IDevicePointService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.AssetQueryVO;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.devicepoint.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private BaseDeviceTypeMapper baseDeviceTypeMapper;



	@Override
	public ApiResult getList(DevicePointQueryVO vo) {
		if (null != vo.getSubsetId() && vo.getSubsetId().intValue()!=0){
//			List<Long> pointIds = devicePointSubsetDetailMapper.getPointIdsBySubsetId(vo.getSubsetId());
//			vo.setPointIds(pointIds);
			int pageNum = vo.getPageNum();
			int pageSize = vo.getPageSize();
			Page page = PageHelper.startPage(pageNum, pageSize);
			List<DevicePointResVO> result = devicePointSubsetDetailMapper.getPointList(vo);
			return ApiResult.success(new PageResult<DevicePointResVO>(page.getTotal(), result, pageSize, pageNum));

		}

		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<DevicePointResVO> result = devicePointMapper.getList(vo);
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
	public ApiResult batchAddDevicePoint(String filePath, String fileName, UserSession userSession) {
		File file = this.getRemoteFile(filePath);
		if (file == null) {
			return ApiResult.error("文件为空!");
		}

		String absoluteNewFilePath = this.uploadFolder + filePath;
		log.info("file#{}", file.getAbsolutePath());
		try {
			ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getDevicePointReader());
			if (linkResult == null) {
				return ApiResult.error("未填写任何点位信息!");
			}
			List<ExcelDevicePointVO> list = linkResult.getEntity().convertToRawTable(ExcelDevicePointVO.class);
			if (list == null || list.size() == 0) {
				return ApiResult.error("未填写任何点位!!");
			}
			log.info("link.size#{}", list.size());
			Map<String, Object> map = this.dealExcelDevicePoint(list, userSession,absoluteNewFilePath);

			if (!linkResult.getValid()) {
				log.error("文件校验失败#" + absoluteNewFilePath);
				map.put("errorPath",absoluteNewFilePath);
				map.put("msg","部分数据存在格式错误");
				//return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
			}
			return ApiResult.success(map);
		} catch (Exception ex) {
			log.error("读取用户上传文件失败#" + absoluteNewFilePath, ex);
			return ApiResult.error(5010,"用户上传模板错误，解析失败！");
		}
	}

	@Override
	public void updateDevicePoint(DevicePoint devicePoint) {
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
	 * @param list
	 * @param userSession
	 * @param absoluteNewFilePath
	 * @return
	 */
	public Map<String, Object> dealExcelDevicePoint(List<ExcelDevicePointVO> list,
							  UserSession userSession,String absoluteNewFilePath) {
		Map<String, Object> map = new HashMap<>();
		//定义失败计数
		int failCount=0;
		String nickname=userSession.getNickName();
		for (ExcelDevicePointVO vo : list) {
			if(vo.toString().contains("[必填]")){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"请补充该行必填数据!",absoluteNewFilePath);
				continue;
			}
			//设为导入测试数据
			vo.setIsTest(1);
			//数据处理逻辑 如果pointCodeChange存在时进行什么操作
			//数据改变 如辖区name改为辖区id 经度和纬度合成gis point_type赋默认值1
			//辖区查询 如果辖区对应的areaId不存在 则给出错误提示
			String areaId=baseAreaMapper.getAreaIdByName(vo.getAreaName());
			//如果辖区id为空 直接在excel中写入错误提示
			if(StringUtils.isBlank(areaId)){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"辖区名称为"+vo.getAreaName()+"的辖区id不存在!",absoluteNewFilePath);
				continue;
			}
			vo.setAreaId(baseAreaMapper.getAreaIdByName(vo.getAreaName()));
			String deviceType=baseDeviceTypeMapper.getTypeIdByName(vo.getDeviceType());
			//如果设备类型对应的id不存在  则写出错误提示信息
			if(StringUtils.isBlank(deviceType)){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"设备类型为"+vo.getDeviceType()+"对应的id不存在!",absoluteNewFilePath);
				continue;
			}
			vo.setDeviceType(deviceType);
			vo.setPointType(1);
			vo.setCreator(nickname);
			vo.setCreateTime(new Date());
			vo.setUpdateTime(new Date());
			//默认将地图来源设为高德
			vo.setMapSource("2");
			//判断数据库中是否存在当前的pointCode及pointCodeChange
			Boolean isExistPointCode = devicePointMapper.getByCode(vo.getPointCode())>0;
			Boolean isExistPointCodeChange =false;
			//如果pointCodeChange不为空的情况
			if(!StringUtils.isBlank(vo.getPointCodeChange())){
				isExistPointCodeChange=devicePointMapper.getByCode(vo.getPointCodeChange())>0;
			}
			failCount=dealData(isExistPointCode,isExistPointCodeChange,absoluteNewFilePath,vo,failCount);
		}
		map.put("failCount",failCount);
		map.put("successCount",list.size()-failCount);
		if(list.size()!=list.size()-failCount){
			map.put("errorPath",absoluteNewFilePath);
		}
		return map;
	}

	/**
	 * 点位数据处理逻辑
	 * @param isExistPointCode 点位编码是否存在
	 * @param isExistPointCodeChange 修改后的点位编码是否存在
	 * @param absoluteNewFilePath 文件绝对路径
	 * @param vo 点位数据
	 * @param failCount 失败条数
	 * @return
	 */
	private int dealData(Boolean isExistPointCode, Boolean isExistPointCodeChange, String absoluteNewFilePath, ExcelDevicePointVO vo, int failCount) {
		//分情况判断 情况1 pointCode和pointCodeChange都为空，做新增
		if(!isExistPointCode&&!isExistPointCodeChange){
			if(!StringUtils.isBlank(vo.getPointCodeChange())){
				vo.setPointCode(vo.getPointCodeChange());
			}
			try {
				devicePointMapper.insertDevicePoint(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据新增失败！",absoluteNewFilePath);
			}

		}
		//pointCode不存在，但是pointCodeChange存在的情况，根据pointCodeChange做修改
		else if(!isExistPointCode&&isExistPointCodeChange){
			vo.setPointCode(vo.getPointCodeChange());
			devicePointMapper.updateByPointCode(vo);
		}
		//pointCode存在，但是pointCodeChange不存在的情况，根据pointCode做修改
		else if(isExistPointCode&&!isExistPointCodeChange){
			if(StringUtils.isBlank(vo.getPointCodeChange())){
				vo.setPointCodeChange(vo.getPointCode());
			}
			try {
				devicePointMapper.updateByPointCodeChange(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);

			}

		}//pointCode和pointCodeChange都存在的请况
		else{
			try {
				//首先删除pointCode的记录，然后修改pointCodeChange的记录
				devicePointMapper.deleteByPointCode(vo.getPointCode());
				//修改pointCodeChange的数据
				vo.setPointCode(vo.getPointCodeChange());
				devicePointMapper.updateByPointCode(vo);
			}catch (Exception e){
				failCount++;
				writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(),"数据修改失败！",absoluteNewFilePath);
			}

		}
			return failCount;
	}


}
