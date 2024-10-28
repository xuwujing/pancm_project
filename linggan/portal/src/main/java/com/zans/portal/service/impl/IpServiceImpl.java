package com.zans.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.IpAllMapper;
import com.zans.portal.model.Asset;
import com.zans.portal.model.IpAll;
import com.zans.portal.model.RadiusEndpointProfile;
import com.zans.portal.service.*;
import com.zans.portal.util.RestTemplateHelper;
import com.zans.portal.vo.asset.branch.req.AssetBranchAssetAddReqVO;
import com.zans.portal.vo.asset.req.ExcelEndpointIpAllVO;
import com.zans.portal.vo.ip.ExcelIpAllVO;
import com.zans.portal.vo.ip.IpSearchVO;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.radius.QzViewRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;
import static com.zans.portal.config.GlobalConstants.*;

@Service
@Slf4j
public class IpServiceImpl extends BaseServiceImpl<IpAll> implements IIpService {

    IpAllMapper ipMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IArpService arpService;



    @Autowired
    ILogOperationService logOperationService;

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @Value("${api.upload.folder}")
    String uploadFolder;

    @Autowired
    IFileService fileService;



    @Autowired
    IRadiusEndPointService endPointService;

    @Autowired
    IRadiusEndPointProfileService endPointProfileService;

    @Autowired
    IAssetService assetService;

    @Autowired
    IAssetBranchAssetService assetBranchAssetService;

    @Resource
    public void setIpMapper(IpAllMapper ipMapper) {
        super.setBaseMapper(ipMapper);
        this.ipMapper = ipMapper;
    }

    @Override
    public PageResult<IpVO> getIpPage(IpSearchVO req) {
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();
        String orderBy = req.getSortOrder();

        Page page = PageHelper.startPage(pageNum, pageSize, orderBy);
        List<IpVO> list = ipMapper.findAllIp(req);
        Map<Object, String> authMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_AUTH_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);

        return new PageResult<IpVO>(page.getTotal(), page.getResult(), pageSize, pageNum);

    }

    @Override
    public IpVO getIp(Integer id) {
        IpVO vo = this.ipMapper.getIp(id);

        Map<Object, String> authMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_AUTH_STATUS);
        Map<Object, String> projectStatusMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_PROJECT_STATUS);
        Map<Object, String> muteNameMap = constantItemService.findItemsMapByDict(MODULE_ARP_MUTE);
        if (vo != null) {
            vo.setIpAuthStatusNameByMap(authMap);
            vo.setProjectStatusNameByMap(projectStatusMap);
            vo.setMuteNameByMap(muteNameMap);
        }
        return vo;
    }

    @Override
    public IpVO findLastAssignIp(Integer area, Integer deviceType) {
        return ipMapper.findLastAssignIp(area, deviceType);
    }

    @Override
    public IpVO findDefaultAssignIp(Integer area, Integer deviceType) {
        return ipMapper.findDefaultAssignIp(area, deviceType);
    }

    @Override
    public int deleteIpByAlloc(Integer allocId) {
        return ipMapper.deleteIpByAlloc(allocId);
    }



    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void doApprove(IpAll ipItem, UserSession session, Integer status, String mark) {

        ipItem.setAuthStatus(status);
        ipItem.setAuthPermitPerson(session.getNickName());
        ipItem.setAuthPermitTime(DateHelper.getNow());
        ipItem.setAuthMark(mark);

        String mac = ipItem.getAuthMac();
        mac = mac.toLowerCase();

        String logContent = JSON.toJSONString(ipItem);

        if (IP_AUTH_REJECT == status) {
            ipItem.setAuthMac(null);
        }
        this.update(ipItem);


        Map<Object, String> authMap = constantItemService.findItemsMapByDict(MODULE_IP_ALL_AUTH_STATUS);
        String statusName = authMap.get(status);

        // 记录日志

    }


    @Override
    public QzViewRespVO findByIp(String ip) {
        return ipMapper.findByIp(ip);
    }


    @Override
    public ApiResult readIpAllFile(String filePath, String fileName,UserSession userSession,Integer assetBranchId,Integer assetGuardLineId) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            return null;
        }

        String absoluteNewFilePath = this.uploadFolder + "/" + filePath;
        log.info("file#{}", file.getAbsolutePath());
        try {
            ExcelEntity linkResult = this.checkFile(fileName, file, absoluteNewFilePath, getIpAllReader());
            if (!linkResult.getValid()){
                log.error("文件校验失败#" + absoluteNewFilePath);
                return ApiResult.error(SERVER_UPLOAD_ERROR,absoluteNewFilePath);
            }

            if (linkResult == null) {
                return ApiResult.error("未填写任何资产!");
            }
            List<ExcelIpAllVO> list = linkResult.getEntity().convertToRawTable(ExcelIpAllVO.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何资产!!");
            }
            log.info("link.size#{}", list.size());
            this.dealExcelIpAll(list,assetBranchId,assetGuardLineId,userSession);

            return ApiResult.success();
        } catch (Exception ex) {
            log.error("读取资产上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR,absoluteNewFilePath);
        }

    }
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void dealExcelIpAll(List<ExcelIpAllVO> list,Integer assetBranchId,Integer assetGuardLineId,UserSession userSession) {


        Asset asset = null;

        for (ExcelIpAllVO excelIpAllVO : list) {
            String ipAddr = excelIpAllVO.getIpAddr();




            //如果是0默认设置到襄阳市公安局这个坐标上
            if (BigDecimal.ZERO.compareTo(new BigDecimal(excelIpAllVO.getLatitude())) == 0) {
                excelIpAllVO.setLatitude("32.045592");
            }
            if (BigDecimal.ZERO.compareTo(new BigDecimal(excelIpAllVO.getLongitude())) == 0) {
                excelIpAllVO.setLongitude("111.989324");
            }

            asset = new Asset();
            BeanUtils.copyProperties(excelIpAllVO,asset);

            asset.setPointName(excelIpAllVO.getPointName());
            asset.setContractorPerson(userSession.getNickName());
            asset.setCreatePerson(userSession.getNickName());
            asset.setCreatorId(userSession.getUserId());
            asset.setUpdateId(userSession.getUserId());
            asset.setDeviceModelDes(excelIpAllVO.getDeviceModelDes());

            Asset uniqueAsset = assetService.uniqueByCondition(new HashMap<String, Object>(4) {{
                put("ipAddr", ipAddr);
            }});
            if (null == uniqueAsset ){
                ///asset.setAlive(2);
                //1.系统-arp with radius;2.系统arp without radius; 3.系统vpn; 4. 人工; 5.导入
                asset.setSource(5);
                assetService.saveSelective(asset);
            } else {
                asset.setId(uniqueAsset.getId());
                assetService.updateSelective(asset);
            }

            if (assetBranchId != null){
                AssetBranchAssetAddReqVO branchAddVO = new AssetBranchAssetAddReqVO();
                branchAddVO.setAssetBranchId(assetBranchId);
                branchAddVO.setAssetId(asset.getId());
                branchAddVO.setIpAddr(ipAddr);
                assetBranchAssetService.groupsAddAsset(branchAddVO,userSession);
            }


        }
    }


    private ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader) throws Exception {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            this.checkInput(sheet, sheetEntity, true, dataStyle);
            result.setValid(sheetEntity.getValid());
            result.setEntity(sheetEntity);

            // 校验不通过，修改文件
            if (!result.getValid()) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("readFile error, close error", ex1);
            }
        }
        return result;
    }

    /**
     * 校验 excel输入，直接保存校验结果到 poi 对象
     * @param poiSheet poi 的sheet
     * @param sheetEntity 数据对象
     * @param writeExcel true，校验结果写入excel； false，不写文件
     */
    private void checkInput(Sheet poiSheet, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
        boolean validResult = true;
        for(ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();

            Row poiRow = poiSheet.getRow(rowIndex);
            log.info("row[{}], {}", rowIndex, (poiRow != null));
            for(String name : excelRow.getData().keySet()) {
                ExcelColumn column = excelRow.getColumn(name);
                int colIndex = column.getCol();
                Object val = column.getValue();
                String[] rules = sheetEntity.getValidateRule(name);
                //数据校验
                ValidateResult result = ValidateHelper.doValidate(val, rules);
                column.setValid(result.getPassed());
                if (result.getPassed()) {
                    continue;
                }
                validResult = false;
                String message = result.getMessages();

                // 更新内存对象的值
                String strVal = (val == null) ? "" : val.toString();
                if (!strVal.contains(message)) {
                    strVal = strVal + message;
                    column.setValue(strVal);
                }
                log.error("validate#{}-{}, {}, {}, {}", rowIndex, colIndex, name, message, strVal);

                log.info("col[{}]#{}, error#{}", colIndex, name, strVal);
                if (!writeExcel) {
                    continue;
                }
                // 更新Excel的值
                Cell poiCell = poiRow.getCell(colIndex);

                if (poiCell == null) {
                    poiCell = poiRow.createCell(colIndex);
                    poiCell.setCellStyle(cellStyle);
                }
                poiCell.setCellValue(strVal);
            }
        }
        sheetEntity.setValid(validResult);
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

    private ExcelSheetReader getIpAllReader() {
        return ExcelSheetReader.builder()
                .headRowNumber(Lists.newArrayList(2))
                .sheetNo(0)
                .notNullFields(Lists.newArrayList("seq,ip_addr,device_model_des,point_name,pointName"))
                .headerClass(ExcelIpAllVO.class).build();
    }

    private ExcelSheetReader getEndpointIpAllReader() {
        return ExcelSheetReader.builder()
                .headRowNumber(Lists.newArrayList(2))
                .sheetNo(0)
                .notNullFields(Lists.newArrayList("ip_addr,point_name,area_name,device_type_name"))
                .headerClass(ExcelEndpointIpAllVO.class).build();
    }

    @Transactional(rollbackFor = RollbackException.class)
     void dealExcelEndpointIpAll(List<ExcelEndpointIpAllVO> list, UserSession userSession) {
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        List<SelectVO> maintainStatusList = constantItemService.findItemsByDict(GlobalConstants.SYS_DICT_KEY_MAINTAIN_STATUS);
        IpAll ipAll = null;
        for (ExcelEndpointIpAllVO vo : list) {

            vo.resetDeviceType(deviceTypeList);
            vo.resetMaintainStatus(maintainStatusList);
            ipAll = new IpAll();
            BeanUtils.copyProperties(vo,ipAll);
//            ipAll.setSource(IP_ALL_SOURCE_IMPORT);
//            ipAll.setCreatePerson(userSession.getNickName());
            List<RadiusEndpointProfile> profileList = endPointProfileService.findByCurIpAddr(vo.getIpAddr());
//            ipAll.setIpUsed(CollectionUtils.isEmpty(profileList) ? IP_ALL_IP_NO_USE : IP_ALL_IP_USED);

            IpAll existIpAll = findByIpAddr(vo.getIpAddr());
            if(existIpAll == null){

                this.saveSelective(ipAll);
            }else {
                ipAll.setId(existIpAll.getId());
                this.updateSelective(ipAll);
            }
        }
    }


    @Override
    public IpAll findByIpAddr(String ip){
        if (StringUtils.isEmpty(ip)){
            return null;
        }
        Example example = new Example(IpAll.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipAddr",ip);
        List<IpAll> ipAllList = ipMapper.selectByExample(example);
        if (ipAllList.size() > 0){
            //此资产已存在t_ip_all中
            return ipAllList.get(0);
        }
        return null;
    }

}
