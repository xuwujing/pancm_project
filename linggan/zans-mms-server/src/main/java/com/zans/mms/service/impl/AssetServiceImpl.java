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
import com.zans.base.util.FileHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.config.AssetConstants;
import com.zans.mms.dao.*;
import com.zans.mms.model.*;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IBaseOrgService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IFileService;
import com.zans.mms.util.ExcelExportUtil;
import com.zans.mms.util.FileZip;
import com.zans.mms.util.UrlUtils;
import com.zans.mms.util.WriteErrorMsgUtil;
import com.zans.mms.vo.asset.*;
import com.zans.mms.vo.asset.diagnosis.*;
import com.zans.mms.vo.asset.subset.AssetExportVO;
import com.zans.mms.vo.devicepoint.ExcelDevicePointVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zans.base.util.FileHelper.createMultilayerFile;

/**
 * AssetServiceImpl
 *
 * @author
 */
@Slf4j
@Service("assetService")
public class AssetServiceImpl extends BaseServiceImpl<Asset> implements IAssetService {


    @Autowired
    private AssetMapper assetMapper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Value("${api.diagnosis.folder:}")
    private String diagnosisFolder;

    @Value("${request.diagnosis.url:}")
    private String diagnosisUrl;

    private final String normal = "正常";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final String dayBegin = " 00:00:00";

    private final String dayEnd = " 23:59:59";

    @Autowired
    IFileService fileService;

    @Autowired
    DevicePointMapper devicePointMapper;

    @Autowired
    BaseDeviceTypeMapper baseDeviceTypeMapper;

    @Autowired
    AssetSubsetDetailMapper assetSubsetDetailMapper;

    @Autowired
    private IBaseOrgService baseOrgService;

    @Autowired
    WriteErrorMsgUtil writeErrorMsgUtil;

    @Autowired
    ExcelExportUtil excelExportUtil;

    @Resource
    private AssetDiagnosisFlagInfoHisDao assetDiagnosisFlagInfoHisDao;

    @Resource
    private AssetDiagnosisFlagInfoExDao assetDiagnosisFlagInfoExDao;

    @Autowired
    private AssetDiagnosisInfoExDao assetDiagnosisInfoExDao;

    @Autowired
    private IConstantItemService constantItemService;

    @Resource
    private SysConstantItemMapper sysConstantItemMapper;

    @Resource
    private SpeedDao speedDao;

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
    public ApiResult flagDiagnosis(AssetDiagnosisInfoExReqVO vo) {
        AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO = new AssetDiagnosisInfoRespVO();
        String assetCode = vo.getAssetCode();
        String traceId = vo.getTraceId();
        //如果traceId不为空，就表明是查询历史数据
        if (!StringUtils.isEmpty(traceId)) {
            assetDiagnosisInfoRespVO = assetMapper.getDiagnosisHisView(assetCode, traceId);
        } else {
            assetDiagnosisInfoRespVO = assetMapper.getDiagnosisView(assetCode);
        }

        List<AssetDiagnosisFlagInfoEx> assetDiagnosisFlagInfoExes = new ArrayList<>();
        List<AssetDiagnosisInfoExVO> assetDiagnosisInfoExVOS = vo.getAssetDiagnosisInfoExVOS();
        AssetDiagnosisFlagInfoHis assetDiagnosisFlagInfoHis = new AssetDiagnosisFlagInfoHis();
        BeanUtils.copyProperties(assetDiagnosisInfoRespVO, assetDiagnosisFlagInfoHis);

        AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO1 = assetMapper.getDiagnosisFlagView(assetCode, assetDiagnosisInfoRespVO.getTraceId());
        if (assetDiagnosisInfoRespVO1 == null) {
            String fileUrl = assetDiagnosisInfoRespVO.getImgUrl();
            fileUrl = fileUrl.replace("\\", "/");
            assetDiagnosisInfoRespVO.setImgUrl(fileUrl);
            String url = diagnosisUrl + fileUrl;
            String desUrl = diagnosisFolder + fileUrl;
            String fileName = FileHelper.getFilename(url);
            String filePath = FileHelper.getFilePath(desUrl, fileName);
            log.info("标记的下载fileName:{},filePath:{},url:{},desUrl:{}", fileName, filePath, url, desUrl);
            createMultilayerFile(filePath);
            try {
                UrlUtils.downloadPicture(url, desUrl);
            } catch (Exception e) {
                log.error("存储图片失败！url:{},desUrl:{},原因是:", url, desUrl, e);
                return ApiResult.error("标记失败！请检查服务配置!");
            }
            assetDiagnosisFlagInfoHis.setImgUrl(fileUrl);
            assetDiagnosisFlagInfoHisDao.insert(assetDiagnosisFlagInfoHis);
        } else {
            assetDiagnosisFlagInfoHisDao.update(assetDiagnosisFlagInfoHis);
            assetDiagnosisFlagInfoExDao.deleteByTraceId(assetCode, assetDiagnosisInfoRespVO.getTraceId());
        }
        if (assetDiagnosisInfoExVOS != null) {
            for (AssetDiagnosisInfoExVO assetDiagnosisInfoExVO : assetDiagnosisInfoExVOS) {
                AssetDiagnosisFlagInfoEx assetDiagnosisFlagInfoEx = new AssetDiagnosisFlagInfoEx();
                BeanUtils.copyProperties(assetDiagnosisInfoExVO, assetDiagnosisFlagInfoEx);
                assetDiagnosisFlagInfoEx.setTraceId(assetDiagnosisInfoRespVO.getTraceId());
                assetDiagnosisFlagInfoEx.setAssetCode(assetDiagnosisInfoRespVO.getAssetCode());
                assetDiagnosisFlagInfoEx.setImgUrl(assetDiagnosisInfoRespVO.getImgUrl());
                assetDiagnosisFlagInfoEx.setDiagnosisResult(assetDiagnosisInfoRespVO.getDiagnosisResult());
                assetDiagnosisFlagInfoEx.setDiagnosisTime(assetDiagnosisInfoRespVO.getDiagnosisTime());
                assetDiagnosisFlagInfoExes.add(assetDiagnosisFlagInfoEx);
            }
            assetDiagnosisFlagInfoExDao.insertBatch(assetDiagnosisFlagInfoExes);
        }
        return ApiResult.success();
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
        return assetSubsetDetailMapper.getByAssetId(id) > 0;
    }

    @Override
    public int deleteByUniqueId(String id) {
        assetSubsetDetailMapper.deleteDetailByAssetId(id);
        return assetMapper.deleteByUniqueId(id);
    }


    //预留方法 处理数据请求 如处理ip存在 但是账号或密码为空的情况
    public Map<String, Object> check(List<ExcelAssetVO> list) {
        Map<String, Object> map = new HashMap<>();
        //无法插入的条数
        int count = 0;
        //无法插入的assetCode列表
        List<String> assetCodeList = new ArrayList<>();
        for (ExcelAssetVO vo : list) {
            //如果ip不为空
            if (!StringUtils.isBlank(vo.getNetworkIp())) {
                //判断账户和密码是否为空  如果存在为空的 不进行插入 并且记录他的pointCode
                Boolean flag = StringUtils.isNotBlank(vo.getDeviceAccount()) && StringUtils.isNotBlank(vo.getDevicePwd());
                if (!flag) {
                    assetCodeList.add(vo.getAssetCode());
                    count++;
                }
            }
        }
        map.put("failCount", count);
        map.put("failAssetCode", assetCodeList);
        return map;
    }

    /**
     * 批量插入资产数据
     *
     * @param filePath    文件路径
     * @param fileName    文件名称
     * @param userSession
     * @return
     */
    @Override
    public List<ExcelAssetVO> batchAddAsset(String filePath, String fileName, UserSession userSession, String type) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            throw  new BusinessException("文件为空!");
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        try {
            ExcelEntity linkResult = fileService.checkFile(fileName, file, absoluteNewFilePath, getAssetReader(type));
            if (linkResult == null) {
                throw new BusinessException("未填写任何资产信息!");
            }
            List<ExcelAssetVO> list = linkResult.getEntity().convertToRawTable(ExcelAssetVO.class);
            if (list == null || list.size() == 0) {
                throw new BusinessException("未填写任何资产信息!");
            }
            //处理资产数据的设备类型方法
            List<ExcelAssetVO> assetVOList = dealAssetDeviceType(list, type);
            if (!linkResult.getValid()) {
                throw new BusinessException("文件校验失败#");
            }
            return assetVOList;
        } catch (Exception ex) {
            log.error("用户上传模板错误或数据格式有误#" + absoluteNewFilePath, ex);
            throw  new BusinessException("用户上传模板错误，解析失败！");
        }
    }


    @Async
    @Override
    public void dealUploadData(Integer operation,Speed speed, List<ExcelAssetVO> assetVOS, String newFileName, UserSession userSession) {
        this.dealExcelAsset(operation,speed,assetVOS, userSession, newFileName);
    }

    /**
     * 资产导入模板下载通用方法
     *
     * @param type
     */
    @Override
    public Map<String, String> downloadTemplate(String type) {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String filePath = this.uploadFolder + "template/资产导入-电警-excel模板.xlsx";
        String fileCnName = "资产电警导入模板" + date + ".xlsx";
        switch (type) {
            case "electricPolice":
                filePath = this.uploadFolder + "template/资产导入-电警-excel模板.xlsx";
                fileCnName = "资产电警导入模板" + date + ".xlsx";
                break;
            case "electronicLabel":
                filePath = this.uploadFolder + "template/资产导入-电子标识-excel模板.xlsx";
                fileCnName = "资产电子标识导入模板" + date + ".xlsx";
                break;
            case "monitor":
                filePath = this.uploadFolder + "template/资产导入-监控-excel模板.xlsx";
                fileCnName = "资产监控导入模板" + date + ".xlsx";
                break;
            case "bayonet":
                filePath = this.uploadFolder + "template/资产导入-卡口-excel模板.xlsx";
                fileCnName = "资产卡口导入模板" + date + ".xlsx";
                break;
            case "trafficSignal":
                filePath = this.uploadFolder + "template/资产导入-信号机-excel模板.xlsx";
                fileCnName = "资产信号机导入模板" + date + ".xlsx";
                break;
            case "inductionScreen":
                filePath = this.uploadFolder + "template/资产导入-诱导屏-excel模板.xlsx";
                fileCnName = "资产诱导屏导入模板" + date + ".xlsx";
                break;
            default:
                break;
        }
        Map<String, String> map = new HashMap<>();
        map.put("filePath", filePath);
        map.put("fileCnName", fileCnName);
        return map;
    }

    //资产数据处理方法  处理数据的设备类型
    private List<ExcelAssetVO> dealAssetDeviceType(List<ExcelAssetVO> list, String type) {
        //用于处理默认设备类型
        List<ExcelAssetVO> assetVOList = new ArrayList<>();
        //如果type为电警时
        String deviceType = null;
        if (AssetConstants.ELECTRIC_POLICE.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.ELECTRIC_POLICE_NAME);
        } else if (AssetConstants.ELECTRONIC_LABEL.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.ELECTRONIC_LABEL_NAME);
        } else if (AssetConstants.MONITOR.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.MONITOR_NAME);
        } else if (AssetConstants.BAYONET.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.BAYONET_NAME);
        } else if (AssetConstants.TRAFFIC_SIGNAL.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.TRAFFIC_SIGNAL_NAME);
        } else if (AssetConstants.INDUCTION_SCREEN.equals(type)) {
            deviceType = baseDeviceTypeMapper.getTypeIdByName(AssetConstants.INDUCTION_SCREEN_NAME);
        } else {
            deviceType = AssetConstants.UNKNOW;
        }
        for (ExcelAssetVO vo : list) {
            vo.setDeviceType(deviceType);
            assetVOList.add(vo);
        }
        return assetVOList;
    }

    /**
     * 处理资产excel数据
     *
     * @param operation 操作类型 1：新增 2：更新 3：新增和更新
     * @param list                从excel读取的数据集合
     * @param userSession         用户信息
     * @param absoluteNewFilePath 上传文件的路径
     * @return
     */
    private Map<String, Object> dealExcelAsset(Integer operation,Speed speed,List<ExcelAssetVO> list, UserSession userSession, String absoluteNewFilePath) {
        Map<String, Object> map = new HashMap<>();
        String nickname = userSession.getNickName();
        //定义失败计数
        int failCount = speed.getFailCount();
        int successCount = speed.getSuccessCount();
        //先根据pointCode查询是否有对应的点位信息 如果没有 就不插入 如果有在做数据校验
        for (ExcelAssetVO vo : list) {
            failCount = speed.getFailCount();
            successCount = speed.getSuccessCount();
            //对有非空项不填的处理
            if (vo.toString().contains("[必填]")) {
                failCount++;
                speed.setFailCount(failCount);
                successCount++;
                speed.setSuccessCount(successCount);
                speedDao.updateByPrimaryKeySelective(speed);
                writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请补充该行必填项！", absoluteNewFilePath);
                continue;
            }
            vo.setImportId(speed.getId());
            if (StringUtils.isBlank(vo.getMaintainStatus())) {
                vo.setMaintainStatus("正常");
            }
            if (StringUtils.isNotBlank(vo.getOrgId())) {
                String orgId = baseOrgService.getOrgIdByOrgName(vo.getOrgId());
                if (StringUtils.isBlank(orgId)) {
                    failCount++;
                    successCount++;
                    speed.setSuccessCount(successCount);
                    speed.setFailCount(failCount);
                    speedDao.updateByPrimaryKeySelective(speed);
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "负责单位名称为" + vo.getOrgId() + "的组织id不存在，请检查机构管理中的数据!", absoluteNewFilePath);
                    continue;
                }
                vo.setOrgId(orgId);
            }
            if (StringUtils.isNotBlank(vo.getPointCode())) {
                Long pointId = devicePointMapper.getIdByCode(vo.getPointCode());
                //存在该pointCode的情况 且拿到了他的id 用于做关联
                if (null == pointId) {
                    //如果pointid为空，跳过此次循环，返回这条数据
                    failCount++;
                    successCount++;
                    speed.setSuccessCount(successCount);
                    speed.setFailCount(failCount);
                    speedDao.updateByPrimaryKeySelective(speed);
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "点位编号" + vo.getPointCode() + "对应的数据为空！请校验后输入！", absoluteNewFilePath);
                    continue;
                }
                vo.setPointId(pointId.toString());
            }
            //判断asset_code和asset_code_change是否存在的情况 用于做数据修改或新增
            Boolean isExistAssetCode = assetMapper.getExistAssetCode(vo.getAssetCode()) > 0;
            Boolean isExistAssetCodeChange = false;
            //如果修改后的资产编码不为空  去查询有没有对应的数据
            if (!StringUtils.isBlank(vo.getAssetCodeChange())) {
                isExistAssetCodeChange = assetMapper.getExistAssetCode(vo.getAssetCodeChange()) > 0;
            }
            //根据资产编码和修改后的资产编码进行方法调用
            dealData(operation,speed,isExistAssetCode, isExistAssetCodeChange, nickname, vo, absoluteNewFilePath);
            //根据两个code存在的情况 对数据进行维护
        }
        map.put("failCount", failCount);
        map.put("successCount", list.size() - failCount);
        if (list.size() != list.size() - failCount) {
            map.put("errorPath", absoluteNewFilePath);
        }
        return map;

    }

    /**
     * 数据处理
     * @param operation
     * @param speed
     * @param isExistAssetCode
     * @param isExistAssetCodeChange
     * @param nickname
     * @param vo
     * @param absoluteNewFilePath
     */
    //根据资产编码和修改后的资产编码进行数据处理
    private void dealData(Integer operation,Speed speed,Boolean isExistAssetCode, Boolean isExistAssetCodeChange, String nickname, ExcelAssetVO vo, String absoluteNewFilePath) {
        //当前条数
        int successCount = speed.getSuccessCount();
        //失败条数
        int failCount = speed.getFailCount();
        //定义新增计数
        int addCount = speed.getAddCount();
        //定义修改计数
        int updateCount = speed.getUpdateCount();
        //分情况判断 情况1 assetCode和assetCodeChange都为空，做新增
        if (!isExistAssetCode && !isExistAssetCodeChange) {
            vo.setCreateTime(new Date());
            vo.setCreator(nickname);
            if (!StringUtils.isBlank(vo.getAssetCodeChange())) {
                vo.setAssetCode(vo.getAssetCodeChange());
            }
            try {
                //是否导入数据标识
                vo.setIsTest(1);
                //必填字段校验
                if (StringUtils.isNotBlank(vo.getPointCode()) && StringUtils.isNotBlank(vo.getOrgId())) {
                    if(operation==2){
                        writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行设备编号，您选择导入方式为数据更新，但在设备列表中不存在设备编号为"+vo.getAssetCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
                    }else {
                        assetMapper.insertAsset(vo);
                    }
                    addCount++;
                    speed.setAddCount(addCount);
                } else {
                    failCount++;
                    speed.setFailCount(failCount);
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行点位编号、负责单位等字段是否填写完整！", absoluteNewFilePath);

                }
            } catch (Exception e) {
                log.error("错误信息#{}",e);
                failCount++;
                speed.setFailCount(failCount);
                writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "数据插入失败", absoluteNewFilePath);
            }finally {
                successCount++;
                speed.setSuccessCount(successCount);
                speedDao.updateByPrimaryKeySelective(speed);
            }
        }//assetCode不存在，但是assetCodeChange存在的情况，根据pointCodeChange做修改
        else if (!isExistAssetCode && isExistAssetCodeChange) {
            try {
                if(operation==1){
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行设备编号，您选择导入方式为数据新增，但在设备列表中已存在设备编号为"+vo.getAssetCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
                }else {
                    vo.setAssetCode(vo.getAssetCodeChange());
                    //查询原数据
                    ExcelAssetVO excelAssetVO = assetMapper.getOneByCode(vo.getAssetCode());
                    excelAssetVO.setImportId(speed.getId());
                    assetMapper.insertAsset(excelAssetVO);
                    assetMapper.updateByAssetCode(vo);
                }
                updateCount++;
                speed.setUpdateCount(updateCount);

            } catch (Exception e) {
                failCount++;
                speed.setFailCount(failCount);
                writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "数据修改失败", absoluteNewFilePath);
            }finally {
                successCount++;
                speed.setSuccessCount(successCount);
                speedDao.updateByPrimaryKeySelective(speed);
            }

        }
        //assetCode存在，但是assetCodeChange不存在的情况，根据pointCode做修改
        else if (isExistAssetCode && !isExistAssetCodeChange) {
            if (StringUtils.isBlank(vo.getAssetCodeChange())) {
                vo.setAssetCodeChange(vo.getAssetCode());
            }
            try {
                if(operation==1){
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行设备编号，您选择导入方式为数据新增，但在设备列表中已存在设备编号为"+vo.getAssetCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
                }else {
                    ExcelAssetVO excelAssetVO = assetMapper.getOneByCode(vo.getAssetCode());
                    excelAssetVO.setImportId(speed.getId());
                    assetMapper.insertAsset(excelAssetVO);
                    assetMapper.updateByAssetCodeChange(vo);
                }
                updateCount++;
                speed.setUpdateCount(updateCount);

            } catch (Exception e) {
                failCount++;
                speed.setFailCount(failCount);
                writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "数据修改失败！", absoluteNewFilePath);
            }finally {
                successCount++;
                speed.setSuccessCount(successCount);
                speedDao.updateByPrimaryKeySelective(speed);
            }
        }
        //assetCode和assetCodeChange都存在的请况
        else {
            try {
                if(operation==1){
                    writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "请检查该行设备编号，您选择导入方式为数据新增，但在设备列表中已存在设备编号为"+vo.getAssetCode()+"的数据，请修改后再进行导入！", absoluteNewFilePath);
                }else {
                    //首先删除assetCode的记录，然后修改assetCodeChange的记录
                    assetMapper.deleteByAssetCode(vo.getAssetCode());
                    //修改assetCodeChange的数据
                    vo.setAssetCode(vo.getAssetCodeChange());
                    ExcelAssetVO excelAssetVO = assetMapper.getOneByCode(vo.getAssetCode());
                    excelAssetVO.setImportId(speed.getId());
                    assetMapper.insertAsset(excelAssetVO);
                    assetMapper.updateByAssetCode(vo);
                }

                updateCount++;
                speed.setUpdateCount(updateCount);

            } catch (Exception e) {
                failCount++;
                speed.setFailCount(failCount);
                writeErrorMsgUtil.writeErrorMsg(vo.getRowIndex(), "数据修改失败！", absoluteNewFilePath);
            }finally {
                successCount++;
                speed.setSuccessCount(successCount);
                speedDao.updateByPrimaryKeySelective(speed);
            }
        }
    }


    /**
     * 数据读取
     *
     * @param type 类型：电警/点位/诱导屏等
     * @return
     */
    private ExcelSheetReader getAssetReader(String type) {
        //根据type类型判断和返回
        //当type是电警时
        if (AssetConstants.ELECTRIC_POLICE.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelElectricPoliceVO.class).build();
        }
        //type为电子标识时
        if (AssetConstants.ELECTRONIC_LABEL.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelElectronicIdentificationVO.class).build();
        }
        //type为监控时
        if (AssetConstants.MONITOR.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelMonitorAssetVO.class).build();
        }
        //type为卡口时
        if (AssetConstants.BAYONET.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelBayonetAssetVO.class).build();
        }
        //type为信号机时
        if (AssetConstants.TRAFFIC_SIGNAL.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelSignalAssetVO.class).build();
        }
        //type为诱导屏时
        if (AssetConstants.INDUCTION_SCREEN.equals(type)) {
            return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                    .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
                    .headerClass(ExcelGuidanceScreenAssetVO.class).build();
        }

        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(2)).sheetNo(0)
                .notNullFields(Lists.newArrayList("asset_code,point_code,device_type,org_id"))
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
        //根据优化需求  只展示可以添加的数据部分 20210602
        List<AssetResVO> result = assetMapper.chooseAssetList(vo);
        //查询可以添加的条数
        Long count = assetMapper.chooseAssetListCount(vo);
        Map<String, Object> map = new HashMap();
        map.put("page", new PageResult<AssetResVO>(page.getTotal(), result, pageSize, pageNum));
        map.put("count", count);
        return ApiResult.success(map);
    }

    @Override
    public List<Asset> getListByCondition(HashMap<String, Object> map) {
        return assetMapper.getListByCondition(map);
    }

    @Override
    public ApiResult getDiagnosisView(String assetCode, String traceId) {
        AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO = new AssetDiagnosisInfoRespVO();
        //如果traceId不为空，就表明是查询历史数据
        if (!StringUtils.isEmpty(traceId)) {
            assetDiagnosisInfoRespVO = assetMapper.getDiagnosisHisView(assetCode, traceId);
        } else {
            assetDiagnosisInfoRespVO = assetMapper.getDiagnosisView(assetCode);
        }
        AssetDiagnosisInfoRespVO assetDiagnosisInfoRespVO1 = null;
        if (assetDiagnosisInfoRespVO != null) {
            assetDiagnosisInfoRespVO1 = assetMapper.getDiagnosisFlagView(assetCode, assetDiagnosisInfoRespVO.getTraceId());
        }
        //查询三天内的
        List<AssetDiagnosisHisRespVO> hisRespVOS = assetMapper.getThreeDaysDiagnosisHisList(assetCode);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("diagnosis_info", assetDiagnosisInfoRespVO);
        resultMap.put("diagnosis_his", hisRespVOS);
        resultMap.put("diagnosis_flag", assetDiagnosisInfoRespVO1);
        return ApiResult.success(resultMap);
    }

    /**
     * 生成文件名称
     *
     * @param date
     * @param type
     * @return
     */
    @Override
    public String generatedFileName(String date, String type) {
        String typeName = null;
        switch (type) {
            case "electricPolice":
                typeName = "电警";
                break;
            case "electronicLabel":
                typeName = "电子标识";
                break;
            case "monitor":
                typeName = "监控";
                break;
            case "bayonet":
                typeName = "卡口";
                break;
            case "trafficSignal":
                typeName = "信号灯";
                break;
            case "inductionScreen":
                typeName = "诱导屏";
                break;
            default:
                break;
        }
        String fileName = typeName + "资产数据导出" + date + ".xls";
        return fileName;
    }

    @Override
    public String exportFlag() {
        List<SelectVO> list = constantItemService.findItemsByDict("faultType");
        Map<Object, String> map = list.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
        List<AssetDiagnosisFlagInfoExVO> exVOS = assetDiagnosisFlagInfoExDao.queryAll(new AssetDiagnosisFlagInfoExVO(simpleDateFormat.format(new Date()) + dayBegin, simpleDateFormat.format(new Date()) + dayEnd));
        String path = diagnosisFolder;///home/release/diagnosis
        String desPath = path + File.separator + "标记数" + exVOS.size() + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        for (AssetDiagnosisFlagInfoExVO exVO : exVOS) {
            String imgUrl = path + exVO.getImgUrl();
            Integer faultTypeResult = exVO.getFaultTypeResult();
            String faultTypeResultName = faultTypeResult == 1 ? "正常" : "异常";
            String faultName = map.get(exVO.getFaultType());
            String desUrl = "";
            String fileName = "";
            if ("异常".equals(faultTypeResultName)) {
                desUrl = desPath + File.separator + faultName;
                fileName = faultName + "_" + exVO.getAssetCode() + "_" + exVO.getDiagnosisTime().substring(11, 13) + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmmss") + ".jpg";//exVO.getDiagnosisTime().substring(11, 13)设备图片时间
            } else {
                desUrl = desPath + File.separator + "正常";
                fileName = normal + "_" + faultName + "_" + exVO.getAssetCode() + "_" + exVO.getDiagnosisTime().substring(11, 13) + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmmss") + ".jpg";
            }
//            System.out.println(fileName);
            createMultilayerFile(desUrl);
            log.info("打包下载的图片,faultName:{},fileName:{},imgUrl:{},desUrl:{}", faultName, fileName, imgUrl, desUrl);
            FileHelper.fileCopy(imgUrl, desUrl + File.separator + fileName);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.MICROSECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (CollectionUtils.isEmpty(exVOS)) {
            createMultilayerFile(desPath);
        }
        String desPathZip = desPath + ".zip";
        try {
            FileZip.ZipCompress(desPath, desPathZip);
        } catch (Exception e) {
            log.error("文件压缩失败!", e);
        }
        return desPathZip;

    }

    @Override
    public String exportFlagByKind(HttpServletResponse response) {
        List<SelectVO> list = constantItemService.findItemsByDict("faultType");
        Map<Object, String> map = list.stream().collect(Collectors.toMap(SelectVO::getItemKey, SelectVO::getItemValue));
        List<AssetDiagnosisFlagInfoExVO> exVOS = assetDiagnosisFlagInfoExDao.queryAll(new AssetDiagnosisFlagInfoExVO(simpleDateFormat.format(new Date()) + dayBegin, simpleDateFormat.format(new Date()) + dayEnd));
        String path = diagnosisFolder;///home/release/diagnosis
        Map<String, List<AssetDiagnosisFlagInfoExVO>> allDevices = exVOS.stream().collect(Collectors.groupingBy(AssetDiagnosisFlagInfoExVO::getAssetCode));
        Map<String, List<AssetDiagnosisFlagInfoExVO>> collect = exVOS.stream().collect(Collectors.groupingBy(AssetDiagnosisFlagInfoExVO::getTraceId));
        String desPath = path + File.separator + "标记总数" + collect.size() + "_" + DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Map<String, String>> finalResult = new HashMap<>();//String1 = time
        Map<String, String> kindAndStatus = new HashMap<>();//String1 = kind  String2 = status
        for (Map.Entry<String, List<AssetDiagnosisFlagInfoExVO>> stringListEntry : allDevices.entrySet()) {

            List<AssetDiagnosisFlagInfoExVO> deviceSizeNowDays = stringListEntry.getValue();
            Map<String, List<AssetDiagnosisFlagInfoExVO>> deviceDiffTime = deviceSizeNowDays.stream().collect(Collectors.groupingBy(AssetDiagnosisFlagInfoExVO::getTraceId));
            Map<String, List<AssetDiagnosisFlagInfoExVO>> deviceDiffTimePic = deviceSizeNowDays.stream().collect(Collectors.groupingBy(AssetDiagnosisFlagInfoExVO::getImgUrl));
            //根据map.size()判断每个设备生成txt文本以及图片的数量
            for (Map.Entry<String, List<AssetDiagnosisFlagInfoExVO>> listEntry : deviceDiffTime.entrySet()) {

                List<AssetDiagnosisFlagInfoExVO> value = listEntry.getValue();//不同时段设备标记的数量
                for (AssetDiagnosisFlagInfoExVO assetDiagnosisFlagInfoExVO : value) {
                    try {
                        String faultName = map.get(assetDiagnosisFlagInfoExVO.getFaultType());
                        if (assetDiagnosisFlagInfoExVO.getFaultTypeResult() != null) {//同一时间段的如果没有对全部种类操作 fault_type_result会存为null
                            String faultTypeResultName = assetDiagnosisFlagInfoExVO.getFaultTypeResult() == 1 ? "正常" : "异常";
                            kindAndStatus.put(faultName, faultTypeResultName);
                        }
                    } catch (NullPointerException nullPointerException) {
                        nullPointerException.getMessage();
                    }
                }
                Map<String, String> cache = new HashMap<>();//String1 = kind  String2 = status
                cache.putAll(kindAndStatus);
                finalResult.put(value.get(0).getDiagnosisTime().substring(0, 13), cache);//根据标记时间命名文件
                kindAndStatus.clear();
            }

            String txtUrl = desPath + File.separator + stringListEntry.getKey() + File.separator + "标记.txt";
            File file = new File(txtUrl);
            createMultilayerFile(desPath + File.separator + stringListEntry.getKey());

            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(finalResult.toString());
                finalResult.clear();
                bw.flush();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Map.Entry<String, List<AssetDiagnosisFlagInfoExVO>> listEntry : deviceDiffTime.entrySet()) {
                String imgUrl = path + listEntry.getValue().get(0).getImgUrl();
                String picName = desPath + File.separator + stringListEntry.getKey() + File.separator + listEntry.getValue().get(0).getDiagnosisTime().substring(0, 13) + ".jpg";
                FileHelper.fileCopy(imgUrl, picName);
            }
        }


        if (CollectionUtils.isEmpty(exVOS)) {
            createMultilayerFile(desPath);
        }
        String desPathZip = desPath + ".zip";
        try {
            FileZip.ZipCompress(desPath, desPathZip);
        } catch (Exception e) {
            log.error("文件压缩失败!", e);
        }
        return desPathZip;
    }

    /**
     * 根据设备类型和单位导出资产数据
     *
     * @param type
     * @param vo
     * @param fileName
     * @return
     */
    @Override
    public String export(String type, AssetExportQueryVO vo, String fileName) {
        String deviceType = null;
        //type转成设备类型对应的编码
        switch (type) {
            case "electricPolice":
                deviceType = "02";
                break;
            case "electronicLabel":
                deviceType = "14";
                break;
            case "monitor":
                deviceType = "04";
                break;
            case "bayonet":
                deviceType = "03";
                break;
            case "trafficSignal":
                deviceType = "24";
                break;
            case "inductionScreen":
                deviceType = "17";
                break;
            default:
                break;
        }
        //写文件逻辑 制定导出模板
        List<String> rowNameList = new ArrayList<>();
        if (deviceType.equals("02") || deviceType.equals("03")) {
            Collections.addAll(rowNameList, "设备编码", "点位编号", "设备方位", "设备功能", "ip地址", "mac地址", "子网掩码",
                    "网关地址", "设备序列号", "设备型号", "设备品牌", "项目名称", "建设年份", "建设单位", "建设单位联系人", "建设单位联系电话", "运维单位",
                    "运维单位联系人", "运维单位联系电话", "检测方式", "维护状态", "设备账号", "设备密码", "安装时间", "车道数", "备注", "负责单位", "点位名称", "辖区名称");
        } else {
            Collections.addAll(rowNameList, "设备编码", "点位编号", "设备方位", "设备功能", "ip地址", "mac地址", "子网掩码",
                    "网关地址", "设备序列号", "设备型号", "设备品牌", "项目名称", "建设年份", "建设单位", "建设单位联系人", "建设单位联系电话", "运维单位",
                    "运维单位联系人", "运维单位联系电话", "检测方式", "维护状态", "设备账号", "设备密码", "安装时间", "备注", "负责单位", "点位名称", "辖区名称");
        }
        //此时写完文件头
        String absoluteNewFilePath = null;
        try {
            absoluteNewFilePath = excelExportUtil.writeHeader("资产列表", rowNameList, fileName);
        } catch (Exception e) {
            log.error("点位数据导出时，写文件头错误{}", e);
        }
        vo.setDeviceType(deviceType);
        List<AssetExportVO> assetExportVOList = assetMapper.getAssetExportData(vo);
        this.writeAssetData(absoluteNewFilePath, assetExportVOList, deviceType);
        return absoluteNewFilePath;
    }

    /* 写入资产数据
     * @param absoluteNewFilePath 文件绝对路径
     * @param assetExportVOList 资产导出实体
     */
    private void writeAssetData(String absoluteNewFilePath, List<AssetExportVO> assetExportVOList, String deviceType) {
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
            for (int i = 0; i < assetExportVOList.size(); i++) {
                Row row = sheetAt.createRow(i + 1);
                int currentCell = 0;
				/*Collections.addAll(rowNameList,"设备编码","点位id","点位编号","设备方位","设备功能","ip","mac地址","子网掩码",
						"网关","设备序列号","设备型号","设备品牌","项目名称","建设年份","建设单位","建设单位联系人","建设单位联系电话","运维单位",
						"运维单位联系人","运维单位联系电话","检测方式","维护状态","设备账号，设备密码","安装时间","车道数","备注","所属单位及设备");*/
                row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getAssetCode());
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
                if (deviceType.equals("02") || deviceType.equals("03")) {
                    row.createCell(currentCell++).setCellValue(null != assetExportVOList.get(i).getLaneNumber() ? assetExportVOList.get(i).getLaneNumber() : 0);
                }
                row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getRemark());
                row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getOrgName());
                row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getPointName());
                row.createCell(currentCell++).setCellValue(assetExportVOList.get(i).getAreaName());
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
     * 资产数据同步
     */
    @Override
    public void synchronousData() {
        int currentAddItemCount = 0;
        //首先对字典中不存在的项目做一次字典新增
        List<String> notExistProjectName = assetMapper.notExistProjectName();
        if(!StringHelper.isEmpty(notExistProjectName)){
            String dictKey = "project_name";
            SysConstantItem sysConstantItem = new SysConstantItem();
            sysConstantItem.setClassType("int");
            sysConstantItem.setDictKey(dictKey);
            for(String projectName : notExistProjectName){
                String currentItemKey =sysConstantItemMapper.getCurrentItemKeyByDictKey(dictKey);
                sysConstantItem.setItemKey(currentItemKey);
                sysConstantItem.setItemValue(projectName);
                sysConstantItem.setOrdinal((byte) 99);
                sysConstantItem.setId(null);
                //插入一条字典
                sysConstantItemMapper.insert(sysConstantItem);
                currentAddItemCount++;
            }
        }
        ///todo 修改项目分类 如修改为示范工程 交管局自建 及 G5 G6 G7
        assetMapper.synchronousItemClassification();
        //无项目分类的修改为交管局自建
        assetMapper.setDefaultItemClassification();
        ///点位同步项目分类
        log.info("本次新增字典条数#{}",currentAddItemCount);
        //资产项目名称转字典
        assetMapper.updateProjectId();
        //字典新增完后进行点位数据的同步
        devicePointMapper.synchronousProjectId();
        devicePointMapper.synchronousItemClassification();
        devicePointMapper.setDefaultProjectId();
        devicePointMapper.synchronousBuildCompany();

    }

    @Override
    public void backupTable() {
        String date = DateHelper.formatDate(new Date(), "yyyyMMddHHmmss");
        String backupTableName = "asset_"+date+"_bak";
        assetMapper.backupTable(backupTableName);
        assetMapper.synchronousData(backupTableName);
    }

    @Override
    public void synchronousProjectName(Asset asset) {
        //判断一下新的项目名称在字典中是否存在
        if(!StringHelper.isEmpty(asset.getProjectName())){
            //如果不存在 新建一下这个字典
            //如果存在 查询对应的字典id
            SysConstantItem sysConstantItem = new SysConstantItem();
            if(!this.existProjectName(asset.getProjectName())){
                String dictKey = "project_name";

                sysConstantItem.setClassType("int");
                sysConstantItem.setDictKey(dictKey);
                String currentItemKey = sysConstantItemMapper.getCurrentItemKeyByDictKey(dictKey);
                sysConstantItem.setItemKey(currentItemKey);
                sysConstantItem.setItemValue(asset.getProjectName());
                sysConstantItem.setOrdinal((byte) 99);
                //插入一条字典
                sysConstantItemMapper.insert(sysConstantItem);

            }else{
                sysConstantItem = sysConstantItemMapper.getSysConstantItem("project_name",asset.getProjectName());
            }


            asset.setProjectId(Integer.parseInt(sysConstantItem.getItemKey()));
            //根据pointId修改项目名称
            DevicePoint devicePoint = new DevicePoint();
            devicePoint.setId(asset.getPointId());
            devicePoint.setProjectId(Integer.parseInt(sysConstantItem.getItemKey()));
            devicePointMapper.updateByPrimaryKeySelective(devicePoint);
        }
    }

    /**
     * 判断项目字典是否存在
     * @param projectName
     * @return
     */
    private boolean existProjectName(String projectName) {

        return sysConstantItemMapper.existProjectName(projectName) >0;
    }


    @Override
    public ApiResult getImportList(AssetQueryVO vo) {
        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<ExcelAssetVO> result = assetMapper.getImportList(vo);
        return ApiResult.success(new PageResult<ExcelAssetVO>(page.getTotal(), result, pageSize, pageNum));
    }

    @Override
    public void updateImportSelective(Asset asset) {
        assetMapper.updateImportSelective(asset);
    }

    @Override
    public void synchronousImportProjectName(Asset asset) {
        //判断一下新的项目名称在字典中是否存在
        if(!StringHelper.isEmpty(asset.getProjectName())){
            //如果不存在 新建一下这个字典
            //如果存在 查询对应的字典id
            SysConstantItem sysConstantItem = new SysConstantItem();
            if(!this.existProjectName(asset.getProjectName())){
                String dictKey = "project_name";

                sysConstantItem.setClassType("int");
                sysConstantItem.setDictKey(dictKey);
                String currentItemKey = sysConstantItemMapper.getCurrentItemKeyByDictKey(dictKey);
                sysConstantItem.setItemKey(currentItemKey);
                sysConstantItem.setItemValue(asset.getProjectName());
                sysConstantItem.setOrdinal((byte) 99);
                //插入一条字典
                sysConstantItemMapper.insert(sysConstantItem);

            }else{
                sysConstantItem = sysConstantItemMapper.getSysConstantItem("project_name",asset.getProjectName());
            }
            asset.setProjectId(Integer.parseInt(sysConstantItem.getItemKey()));
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importConfig(AssetQueryVO queryVO, UserSession userSession) {
        //查询出本次的所有点位信息
        List<ExcelAssetVO> excelAssetVOList = assetMapper.getByImportId(queryVO.getImportId());
        if(!StringHelper.isEmpty(excelAssetVOList)){
            for(ExcelAssetVO excelAssetVO : excelAssetVOList){
                excelAssetVO.setCreator(userSession.getUserName());
                if(excelAssetVO.getOperation()==1){
                    //插入
                    assetMapper.insertOne(excelAssetVO);
                }
                if(excelAssetVO.getOperation()==2){
                    //修改
                    assetMapper.updateExcelAssetVO(excelAssetVO);
                }
            }
        }
    }
}
