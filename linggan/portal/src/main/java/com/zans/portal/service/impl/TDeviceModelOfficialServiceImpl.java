package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.exception.RollbackException;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.TDeviceModelOfficialMapper;
import com.zans.portal.model.AssetProfile;
import com.zans.portal.model.RadiusEndpointProfile;
import com.zans.portal.model.SysBrand;
import com.zans.portal.model.TDeviceModelOfficial;
import com.zans.portal.service.*;
import com.zans.portal.vo.device.DeviceResponseVO;
import com.zans.portal.vo.device.DeviceSearchVO;
import com.zans.portal.vo.device.ExcelUnknownDeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;

/**
 * @author yanghanjin
 */
@Service
@Slf4j
public class TDeviceModelOfficialServiceImpl extends BaseServiceImpl<TDeviceModelOfficial> implements ITDeviceModelOfficialService {

    TDeviceModelOfficialMapper tDeviceModelOfficialMapper;

    @Value("${api.upload.folder}")
    String uploadFolder;
    @Autowired
    IRadiusEndPointProfileService radiusEndPointProfileService;
    @Autowired
    IAssetProfileService assetProfileService;
    @Autowired
    IDeviceTypeService deviceTypeService;
    @Autowired
    ISysBrandService brandService;

    @Resource
    public void settDeviceModelOfficialMapper(TDeviceModelOfficialMapper tDeviceModelOfficialMapper) {
        super.setBaseMapper(tDeviceModelOfficialMapper);
        this.tDeviceModelOfficialMapper = tDeviceModelOfficialMapper;
    }

    @Override
    public PageResult<DeviceResponseVO> getOfficialPage(DeviceSearchVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<DeviceResponseVO> list = tDeviceModelOfficialMapper.findOfficialList(reqVO);
        return new PageResult<DeviceResponseVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public List<ExcelUnknownDeviceVO> getUnknownDevicePage() {
        return tDeviceModelOfficialMapper.findUnknownDevicePage();
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public ApiResult batchAddDevice(String filePath, String fileName, UserSession userSession) {
        File file = this.getRemoteFile(filePath);
        if (file == null) {
            return null;
        }
        String absoluteNewFilePath = this.uploadFolder + filePath;
        log.info("file#{}", file.getAbsolutePath());
        try {
            ExcelEntity linkResult = checkFile(fileName, file, absoluteNewFilePath, getAssetReader());
            if (!linkResult.getValid()) {
                log.error("文件校验失败#" + absoluteNewFilePath);
                return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
            }
            if (linkResult == null) {
                return ApiResult.error("未填写任何设备模型!");
            }
            List<ExcelUnknownDeviceVO> list = linkResult.getEntity().convertToRawTable(ExcelUnknownDeviceVO.class);
            if (list == null || list.size() == 0) {
                return ApiResult.error("未填写任何设备模型!!");
            }
            log.info("link.size#{}", list.size());
            int ct = 0;
            List<SelectVO> typeList = deviceTypeService.findDeviceTypeToSelect();
            List<SysBrand> brandList = brandService.getAll();
            for (ExcelUnknownDeviceVO dv:list) {
                ct = tDeviceModelOfficialMapper.findOffcialCountByCode(dv.getModelCode(),null);
                if (ct>0){
                    continue;
                }
                List<SysBrand> curBrand = brandList.stream().filter(it->it.getBrandName().equals(dv.getCompany())).collect(Collectors.toList());
                if (curBrand == null || curBrand.size()==0){
                    continue;
                }
                List<SelectVO> curType = typeList.stream().filter(it->it.getItemValue().equals(dv.getDeviceTypeName())).collect(Collectors.toList());
                if (curType == null || curType.size()==0){
                    continue;
                }
                TDeviceModelOfficial modelOfficial= new TDeviceModelOfficial();
                modelOfficial.setBrandId(curBrand.get(0).getBrandId());
                modelOfficial.setCompany(curBrand.get(0).getCompany());
                modelOfficial.setDeviceTypeId(Integer.parseInt(curType.get(0).getItemKey().toString()));
                modelOfficial.setMute("哑终端(前端设备)".equals(dv.getMute())?1:0);
                modelOfficial.setModelCode(dv.getModelCode());
                modelOfficial.setModelDes(dv.getModelDes());
                modelOfficial.setRemark(dv.getRemark());
                int c=tDeviceModelOfficialMapper.insert(modelOfficial);
                log.info("insert DeviceModelOfficial = {} ",c);
                //  update radius_endpoint_profile,asset_profile表的cur_device_type
                RadiusEndpointProfile endpointProfile =radiusEndPointProfileService.getById(Integer.valueOf(dv.getEndpointId()));
                if (endpointProfile != null){
                    endpointProfile.setCurDeviceType(modelOfficial.getDeviceTypeId());
                    radiusEndPointProfileService.updateSelective(endpointProfile);
                }
                AssetProfile assetProfile = assetProfileService.findByIdAddr(dv.getIpAddr());
                if (assetProfile != null){
                    assetProfile.setCurDeviceType(modelOfficial.getDeviceTypeId());
                    assetProfileService.updateSelective(assetProfile);
                }
            }
            return ApiResult.success();
        } catch (Exception ex) {
            log.error("读取设备类型上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }

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
    public int findOffcialCountByCode(String modelCode, Integer id) {
        return tDeviceModelOfficialMapper.findOffcialCountByCode(modelCode, id);
    }

    private ExcelSheetReader getAssetReader() {
        return ExcelSheetReader.builder().headRowNumber(Lists.newArrayList(1)).sheetNo(0)
                .notNullFields(Lists.newArrayList("company,model_code,model_des,device_type_name,remark"))
                .headerClass(ExcelUnknownDeviceVO.class).build();
    }

    private ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader)
            throws Exception {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        OutputStream os = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(file);
            workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook, reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            this.checkInput(workbook, sheetEntity, true, dataStyle);
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
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception ex1) {
                log.error("readFile error, close error", ex1);
            }
        }
        return result;
    }
    /**
     * 校验 excel输入，直接保存校验结果到 poi 对象
     *
     * @param sheetEntity 数据对象
     * @param writeExcel  true，校验结果写入excel； false，不写文件
     */
    private void checkInput( Workbook workbook, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
        boolean validResult = true;
        Sheet poiSheet = workbook.getSheetAt(0);
        for (ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();

            Row poiRow = poiSheet.getRow(rowIndex);
            log.info("row[{}], {}", rowIndex, (poiRow != null));
            for (String name : excelRow.getData().keySet()) {
                ExcelColumn column = excelRow.getColumn(name);
                int colIndex = column.getCol();
                Object val = column.getValue();
                String[] rules = sheetEntity.getValidateRule(name);
                // 数据校验
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
                Cell poiCell = null;//poiRow.getCell(colIndex);

                if (poiCell == null) {
                    poiCell = poiRow.createCell(colIndex);
                    poiCell.setCellStyle(cellStyle);
                }
                poiCell.setCellValue(strVal);
            }

        }
        sheetEntity.setValid(validResult);
    }

}
