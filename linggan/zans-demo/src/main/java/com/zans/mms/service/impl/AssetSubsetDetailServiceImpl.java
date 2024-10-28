package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.ReqCountLockCacheHelper;
import com.zans.base.util.RestTemplateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.MMSConstants;
import com.zans.mms.dao.guard.AssetMapper;
import com.zans.mms.dao.mms.AssetSubsetDetailMapper;
import com.zans.mms.model.AssetSubsetDetail;
import com.zans.mms.service.IAssetSubsetDetailService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.vo.asset.AssetResVO;
import com.zans.mms.vo.asset.subset.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 *  AssetSubsetDetailServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("assetSubsetDetailService")
public class AssetSubsetDetailServiceImpl extends BaseServiceImpl<AssetSubsetDetail> implements IAssetSubsetDetailService {


	@Autowired
	private AssetSubsetDetailMapper assetSubsetDetailMapper;

	@Autowired
	private AssetMapper assetMapper;

	@Autowired
	private ExcelExportUtil excelExportUtil;

	@Autowired
	RestTemplateHelper restTemplateHelper;

	@Autowired
	IConstantItemService constantItemService;

	@Autowired
	ReqCountLockCacheHelper reqCountLockCacheHelper;

	@Resource
	public void setAssetSubsetDetailMapper(AssetSubsetDetailMapper mapper) {
		super.setBaseMapper(mapper);
		this.assetSubsetDetailMapper = mapper;
	}



	@Override
	public ApiResult groupList(AssetSubsetDetaiQueryReqVO reqVO) {
		int pageNum = reqVO.getPageNum();
		int pageSize = reqVO.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);

		List<AssetResVO> result = assetSubsetDetailMapper.groupList(reqVO);

		return ApiResult.success(new PageResult<AssetResVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int groupsAddAsset(AssetSubsetDetailAddReqVO req, UserSession userSession) {
		Long subsetId = req.getSubsetId();
		AssetSubsetDetail subsetDetail ;
		for (Long assetId : req.getAssetIds()) {
			subsetDetail = new AssetSubsetDetail();
			subsetDetail.setAssetId(assetId);
			subsetDetail.setSubsetId(subsetId);
			subsetDetail.setCreator(userSession.getUserName());
			subsetDetail.setCreateTime(new Date());
			subsetDetail.setUpdateTime(new Date());
			assetSubsetDetailMapper.insert(subsetDetail);
		}
		return 1;
	}

	/**
	 * 根据条件添加资产子集数据
	 * @param reqVO
	 * @param userSession
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void groupsAddAssetByCondition(AssetSubsetDetailAddByConditionReqVO reqVO, UserSession userSession) {
		Long subsetId = reqVO.getSubsetId();
		AssetSubsetDetail subsetDetail ;
		List<Long> assetList=new ArrayList<>();
		//直接把reqVo作为查询条件 查询所有符合条件的资产信息
		List<Long> sourceList = assetMapper.getAssetByCondition(reqVO);
		//去重 查询当前分组id中的所有assetid 然后去掉这部分
		List<Long> targetList = assetSubsetDetailMapper.getAssetIdBySubsetId(subsetId);
		//得到去重后的结果
		assetList= removeSame(sourceList, targetList);
		for (Long assetId:assetList) {
			subsetDetail=new AssetSubsetDetail();
			subsetDetail.setSubsetId(subsetId);
			subsetDetail.setAssetId(assetId);
			subsetDetail.setCreator(userSession.getUserName());
			subsetDetail.setCreateTime(new Date());
			subsetDetail.setUpdateTime(new Date());
			assetSubsetDetailMapper.insert(subsetDetail);
		}

	}

	/**
	 * 资产子集数据文件导出
	 * @param fileName 文件名称
	 * @return
	 */
	@Override
	public String exportFile(String fileName) {
		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList,"名称","点位类型","辖区","点位信息","方位","点位编号","设备编号","ip地址","运维单位","在线状态","维护状态");
		//此时写完文件头
		String absoluteNewFilePath= null;
		try {
			absoluteNewFilePath = excelExportUtil.writeHeader("资产子集列表",rowNameList,fileName);
		} catch (Exception e) {
			log.error("资产子集数据导出时，写文件头错误{}",e);
		}
		//进行数据查询 后续做数据插入操作
		List <AssetSubsetDetailExportVO> assetSubsetDetailExportVOList =assetSubsetDetailMapper.selectExportData();
		writeData(absoluteNewFilePath,assetSubsetDetailExportVOList);
		return absoluteNewFilePath;
	}

	/**
	 * 资产子集中资产数据导出
	 * @param subsetId 子集id
	 * @param fileName 文件名称
	 * @return
	 */
	@Override
	public String export(String subsetId, String fileName) {
		//写文件逻辑 制定导出模板
		List<String> rowNameList = new ArrayList<>();
		Collections.addAll(rowNameList,"设备编码","点位id","点位编号","设备方位","设备功能","ip","mac地址","子网掩码",
				"网关","设备序列号","设备型号","设备品牌","项目名称","建设年份","建设单位","建设单位联系人","建设单位联系电话","运维单位",
				"运维单位联系人","运维单位联系电话","检测方式","维护状态","设备账号","设备密码","安装时间","车道数","备注","所属单位及设备");

		//此时写完文件头
		String absoluteNewFilePath= null;
		try {
			absoluteNewFilePath=excelExportUtil.writeHeader("巡检子集列表",rowNameList,fileName);
		}catch (Exception e){
			log.error("点位数据导出时，写文件头错误{}",e);
		}
		List<AssetExportVO> assetExportVOList= assetSubsetDetailMapper.getExportData(subsetId);
		this.writeAssetData(absoluteNewFilePath,assetExportVOList);
		return absoluteNewFilePath;
	}

	/**
	 * 写入资产数据
	 * @param absoluteNewFilePath 文件绝对路径
	 * @param assetExportVOList 资产导出实体
	 */
	private void writeAssetData(String absoluteNewFilePath, List<AssetExportVO> assetExportVOList) {
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
			for(int i =0;i<assetExportVOList.size();i++){
				Row row = sheetAt.createRow(i+1);
				int currentCell=0;
				/*Collections.addAll(rowNameList,"设备编码","点位id","点位编号","设备方位","设备功能","ip","mac地址","子网掩码",
						"网关","设备序列号","设备型号","设备品牌","项目名称","建设年份","建设单位","建设单位联系人","建设单位联系电话","运维单位",
						"运维单位联系人","运维单位联系电话","检测方式","维护状态","设备账号，设备密码","安装时间","车道数","备注","所属单位及设备");*/
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getAssetCode());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getPointId());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getPointCode());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceDirection());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceSubType());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getNetworkIp());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getNetworkMac());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getNetworkMask());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getNetworkGeteway());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceSn());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceModelDes());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceModelBrand());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getProjectName());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getBuildYear());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getBuildCompany());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getBuildContact());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getBuildPhone());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getMaintainCompany());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getMaintainContact());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getMaintainPhone());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDetectMode());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getMaintainStatus());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDeviceAccount());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getDevicePwd());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getInstallTime());
				row.createCell(currentCell++).setCellValue(null!=assetExportVOList.get(i).getLaneNumber()?assetExportVOList.get(i).getLaneNumber():0);
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getRemark());
				row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getAffiliatedUnits());

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
	 * @param assetSubsetDetailExportVOList 数据列表
	 */
	public void writeData(String absoluteNewFilePath, List<AssetSubsetDetailExportVO> assetSubsetDetailExportVOList) {
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
			for(int i =0;i<assetSubsetDetailExportVOList.size();i++){
				Row row = sheetAt.createRow(i+1);
				int currentCell=0;
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getSubsetName());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getDeviceType());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getAreaName());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getPointName());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getDeviceDirection());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getPointCode());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getAssetCode());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getIp());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getMaintainCompany());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getOnlineStatus());
				row.createCell(currentCell++).setCellValue(assetSubsetDetailExportVOList.get(i).getMaintainStatus());
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


	@Transactional(rollbackFor = Exception.class)
	@Override
	public int removeAsset(AssetSubsetDetailAddReqVO reqVO) {
		for (Long assetId : reqVO.getAssetIds()) {
		     int count = assetSubsetDetailMapper.removeAsset(reqVO.getSubsetId(),assetId);
		}
		return 1;
	}

	/**
	 * 第一组数据对第二组数据去重处理
	 * @param sourceList 源数据列表
	 * @param targetList 需要去掉的数据列表
	 * @return
	 */
	public List<Long> removeSame(List<Long> sourceList,List<Long> targetList){
		List<Long> assetList=new ArrayList<>();
		HashSet sourceHashSet= new HashSet(sourceList);
		HashSet targetHashSet = new HashSet(targetList);
		sourceHashSet.removeAll(targetHashSet);
		assetList.addAll(sourceHashSet);
		return assetList;
	}

	@Override
	public ApiResult pingAndStats(AssetSubsetPingReqVO reqVO, UserSession userSession) {
		if (reqCountLockCacheHelper.isReqLocked(MMSConstants.SCAN_STATS_PING_URI)){
			return ApiResult.error("一键ping,请求太频繁，请稍后重试");
		}
		ApiResult apiResult;
		try {
			AssetSubsetDetaiQueryReqVO detaiQueryReqVO = new AssetSubsetDetaiQueryReqVO();
			BeanUtils.copyProperties(reqVO, detaiQueryReqVO);
			List<AssetResVO> list = assetSubsetDetailMapper.groupList(detaiQueryReqVO);
			StringBuffer ipListStr = new StringBuffer();
			for (AssetResVO vo : list) {
				if (!StringUtils.isEmpty(vo.getNetworkIp())) {
					ipListStr.append(vo.getNetworkIp()).append(" ");
				}
			}
			if (ipListStr.length() < 6) {
				return ApiResult.error("一键ping出错,无ip");
			}
			HashMap<String, Object> map = new HashMap<>(8);
			map.put("subset", reqVO.getSubsetId());
			map.put("ipListStr", ipListStr.toString());
			String scanIp = constantItemService.getConstantValueByKey(MMSConstants.SCAN_API);

			log.info("请求url:{},请求参数:{}", scanIp + MMSConstants.SCAN_STATS_PING_URI, map);
			long startLong = System.currentTimeMillis();
			reqCountLockCacheHelper.increaseReqAttempt(MMSConstants.SCAN_STATS_PING_URI);
			apiResult = restTemplateHelper.postForObject(scanIp + MMSConstants.SCAN_STATS_PING_URI, map, 10 * 60, 10 * 60);
			log.info("请求url:{},请求参数:{},返回结果:{}, 请求耗时:{}", scanIp + MMSConstants.SCAN_STATS_PING_URI, map, apiResult, System.currentTimeMillis() - startLong);
			if(null != apiResult && apiResult.getCode()==0){
				int onlineCount = (int)apiResult.getData();
				Map<String, Object> result = MapBuilder.getBuilder()
						.put("onlineCount", onlineCount)
						.put("offlineCount", list.size()-onlineCount)
						.build();
				return ApiResult.success(result);
			}
		} finally {
			reqCountLockCacheHelper.decreaseReqAttempt(MMSConstants.SCAN_STATS_PING_URI);
		}

		return ApiResult.error("一键ping出错!");
	}


}
