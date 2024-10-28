package com.zans.portal.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.portal.dao.AssetBaselineDao;
import com.zans.portal.dao.DeviceTypeMapper;
import com.zans.portal.model.AssetBaseline;
import com.zans.portal.model.DeviceType;
import com.zans.portal.service.IAssetBaselineService;
import com.zans.portal.service.IRadiusEndPointService;
import com.zans.portal.vo.AssetBaselineVO;
import com.zans.portal.vo.AssetBaselineVersionVO;
import com.zans.portal.vo.asset.baseline.AssetBaselineExcelVO;
import com.zans.portal.vo.projectStatusEnum;
import com.zans.portal.vo.radius.QzRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.EnumErrorCode.SERVER_UPLOAD_ERROR;


/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:40
 */
@Service("assetBaselineService")
@Slf4j
public class AssetBaselineServiceImpl extends BaseServiceImpl<AssetBaseline> implements IAssetBaselineService {

    @Autowired
    IRadiusEndPointService radiusEndPointService;

    AssetBaselineDao assetBaselineDao;

    @Autowired
    DeviceTypeMapper deviceTypeMapper;


    @Value("${api.upload.folder}")
    String uploadFolder;


    @Resource
    public void setAssetBaselineDao(AssetBaselineDao assetBaselineDao) {
        super.setBaseMapper(assetBaselineDao);
        this.assetBaselineDao = assetBaselineDao;
    }


    /**
     * 通过ID查询单条数据
     *
     * @param ip 主键
     * @return 实例对象
     */
    @Override
    public AssetBaselineVO queryByIp(String ip) {
        AssetBaselineVO assetBaselineVO = this.assetBaselineDao.queryByIp(ip);
        if (assetBaselineVO != null) {
            List<DeviceType> deviceTypes = deviceTypeMapper.selectAll();
            for (DeviceType deviceType : deviceTypes) {
                if (deviceType.getTypeId().equals(assetBaselineVO.getDeviceType())) {
                    assetBaselineVO.setDeviceTypeName(deviceType.getTypeName());
                }
            }
            Integer status = assetBaselineVO.getProjectStatus();
            if (status != null) {
                if (status == projectStatusEnum.IN_CONSTRUCTION.getCode()) {
                    assetBaselineVO.setProjectStatusName(projectStatusEnum.IN_CONSTRUCTION.getDesc());
                } else if (status == projectStatusEnum.QUALITY_GUARANTEE.getCode()) {
                    assetBaselineVO.setProjectStatusName(projectStatusEnum.QUALITY_GUARANTEE.getDesc());
                } else if (status == projectStatusEnum.OVER_GUARANTEE.getCode()) {
                    assetBaselineVO.setProjectStatusName(projectStatusEnum.OVER_GUARANTEE.getDesc());
                } else {
                    assetBaselineVO.setProjectStatusName(projectStatusEnum.MAINTENANCE.getDesc());
                }
            } else {
                assetBaselineVO.setProjectStatusName("");
            }
            return assetBaselineVO;
        } else {
            return new AssetBaselineVO();
        }

    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(AssetBaselineVO assetBaseline) {
        int pageNum = assetBaseline.getPageNum();
        int pageSize = assetBaseline.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AssetBaselineVO> result = assetBaselineDao.queryAll(assetBaseline);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));
    }

    /**
     * 基线历史列表
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult historyBaselineList(AssetBaselineVersionVO assetBaseline) {
        int pageNum = assetBaseline.getPageNum();
        int pageSize = assetBaseline.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AssetBaselineVersionVO> result = assetBaselineDao.historyBaselineList(assetBaseline);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));

    }


    /**
     * 新增数据
     *
     * @param assetBaselineVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insertOne(AssetBaselineVO assetBaselineVO) {
        AssetBaseline assetBaseline = new AssetBaseline();
        BeanUtils.copyProperties(assetBaselineVO, assetBaseline);
        assetBaseline.setCreateTime(DateHelper.getDateTime(new Date()));
        assetBaseline.setUpdateTime(DateHelper.getDateTime(new Date()));
        String latitude = assetBaseline.getLatitude();
        String longitude = assetBaseline.getLongitude();
        if("".equals(latitude)){
            assetBaseline.setLatitude(null);
        }
        if("".equals(longitude)){
            assetBaseline.setLongitude(null);
        }
        return assetBaselineDao.insert(assetBaseline);
    }

    /**
     * 修改数据
     *
     * @param assetBaselineVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(AssetBaselineVO assetBaselineVO) {
        AssetBaseline assetBaseline = new AssetBaseline();
        BeanUtils.copyProperties(assetBaselineVO, assetBaseline);
        return assetBaselineDao.update(assetBaseline);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.assetBaselineDao.deleteById(id) > 0;
    }

    /**
     * 基线变更历史记录展示
     *
     * @param ip ip地址
     * @return
     */
    @Override
    public ApiResult historyList(String ip) {
        //通过ip把基线变更历史数据查询
        List<AssetBaselineVersionVO> assetBaselineVOList = assetBaselineDao.historyList(ip);
        return ApiResult.success(assetBaselineVOList);
    }

    /**
     * 准入放行界面的比较信息查基线表
     *
     * @param id 终端id
     * @return
     */
    @Override
    public ApiResult compareBaseLine(Long id) {
        //通过id查询当前设备信息
        QzRespVO respVO = radiusEndPointService.findQzById(id.intValue());
        //根据当前设备的ip和mac查询基线表中的数据
        String ipAddr = respVO.getIpAddr();
        String mac = respVO.getMac();
        List<AssetBaselineVO> baseLineList = assetBaselineDao.getByIpOrMac(ipAddr, mac);
        Map<String, Object> map = MapBuilder.getBuilder()
                .put("current", respVO)
                .put("baseLine", baseLineList)
                .build();
        return ApiResult.success(map);
    }

    @Override
    public List<AssetBaseline> findByIpMac(String username, String curIpAddr) {
        return assetBaselineDao.findByIpMac(username, curIpAddr);
    }

    @Override
    public AssetBaselineVO getByIp(String ipAddr) {
        return assetBaselineDao.getByIp(ipAddr);
    }

    @Override
    public List<AssetBaselineVO> getByMac(String mac) {
        return assetBaselineDao.getByMac(mac);
    }

    @Override
    public void unbindByIpAddr(String ip) {
        assetBaselineDao.unbindByIpAddr(ip);

    }

    @Override
    public ApiResult readExcel(MultipartFile file, InputStream inputStream, UserSession userSession) {
        List<AssetBaseline> assetBaselines = assetBaselineDao.selectAll();
        List<DeviceType> deviceTypes = deviceTypeMapper.selectAll();
        try {
            String filename = file.getOriginalFilename();
            System.out.println(filename);
            final XSSFWorkbook tpWorkbook = new XSSFWorkbook(inputStream);
            Sheet sheet = tpWorkbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < 24; i++) {
                try {
                    if (headerRow.getCell(i).getStringCellValue() == null) {
                        return ApiResult.error("模板错误!");
                    }
                } catch (NullPointerException nullPointerException) {

                }
            }
            if (headerRow.getCell(0).getStringCellValue().equals("ipAddr")
                    && headerRow.getCell(1).getStringCellValue().equals("mac地址")
                    && headerRow.getCell(2).getStringCellValue().equals("curModelDes")
                    && headerRow.getCell(3).getStringCellValue().equals("品牌")
                    && headerRow.getCell(4).getStringCellValue().equals("当前设备类型")
                    && headerRow.getCell(5).getStringCellValue().equals("curServerOs")
                    && headerRow.getCell(6).getStringCellValue().equals("开放端口")
                    && headerRow.getCell(7).getStringCellValue().equals("nasIp")
                    && headerRow.getCell(8).getStringCellValue().equals("nasPortId")
                    && headerRow.getCell(9).getStringCellValue().equals("虚拟局域网网络")
                    && headerRow.getCell(10).getStringCellValue().equals("线路名称")
                    && headerRow.getCell(11).getStringCellValue().equals("经度")
                    && headerRow.getCell(12).getStringCellValue().equals("纬度")
                    && headerRow.getCell(13).getStringCellValue().equals("项目名称")
                    && headerRow.getCell(14).getStringCellValue().equals("承建单位")
                    && headerRow.getCell(15).getStringCellValue().equals("项目状态")
                    && headerRow.getCell(16).getStringCellValue().equals("承建单位联系人")
                    && headerRow.getCell(17).getStringCellValue().equals("承建单位联系电话")
                    && headerRow.getCell(18).getStringCellValue().equals("维护单位")
                    && headerRow.getCell(19).getStringCellValue().equals("维护联系人")
                    && headerRow.getCell(20).getStringCellValue().equals("维护联系电话")
                    && headerRow.getCell(21).getStringCellValue().equals("mapCatalogueId")
                    && headerRow.getCell(22).getStringCellValue().equals("ip/mac是否绑定")
                    && headerRow.getCell(23).getStringCellValue().equals("审批意见")
            ) {
                int i = 1;
                boolean flag = true;
                List<AssetBaseline> entityList = new ArrayList<>();
                AssetBaseline assetBaseline;
                while (flag) {
                    Row row = sheet.getRow(i++);
                    for (int index = 0; index < 24; index++) {
                        try {
                            row.getCell(index).setCellType(CellType.STRING);
                        } catch (NullPointerException nullPointerException) {
                        }
                    }
                    if (row != null) {
                        assetBaseline = new AssetBaseline();
                        try {
                            Cell ipAddr = row.getCell(0);
                            Cell username = row.getCell(1);
                            Cell modelDes = row.getCell(2);
                            Cell brandName = row.getCell(3);
                            Cell deviceType = row.getCell(4);
                            Cell serverOs = row.getCell(5);
                            Cell openPort = row.getCell(6);
                            Cell nasIp = row.getCell(7);
                            Cell nasPortId = row.getCell(8);
                            Cell vlan = row.getCell(9);
                            Cell pointName = row.getCell(10);
                            Cell longitude = row.getCell(11);
                            Cell latitude = row.getCell(12);
                            Cell projectName = row.getCell(13);
                            Cell contractor = row.getCell(14);
                            Cell projectStatus = row.getCell(15);
                            Cell contractorPerson = row.getCell(16);
                            Cell contractorPhone = row.getCell(17);
                            Cell maintainCompany = row.getCell(18);
                            Cell maintainPerson = row.getCell(19);
                            Cell maintainPhone = row.getCell(20);
                            Cell mapCatalogueId = row.getCell(21);
                            Cell bindStatus = row.getCell(22);
                            Cell remark = row.getCell(23);

                            assetBaseline.setIpAddr(ipAddr == null ? "" : ipAddr.getStringCellValue());
                            assetBaseline.setMac(username == null ? "" : username.getStringCellValue());
                            assetBaseline.setModelDes(modelDes == null ? "" : modelDes.getStringCellValue());
                            assetBaseline.setBrandName(brandName == null ? "" : brandName.getStringCellValue());
                            assetBaseline.setServerOs(serverOs == null ? "" : serverOs.getStringCellValue());
                            assetBaseline.setOpenPort(openPort == null ? "" : openPort.getStringCellValue());
                            assetBaseline.setNasIp(nasIp == null ? "" : nasIp.getStringCellValue());
                            assetBaseline.setNasPortId(nasPortId == null ? "" : nasPortId.getStringCellValue());
                            assetBaseline.setVlan(vlan == null ? "" : vlan.getStringCellValue());
                            assetBaseline.setPointName(pointName == null ? "" : pointName.getStringCellValue());
                            assetBaseline.setLongitude(longitude == null ? null : longitude.getStringCellValue());
                            assetBaseline.setLatitude(latitude == null ? null : latitude.getStringCellValue());
                            assetBaseline.setProjectName(projectName == null ? "" : projectName.getStringCellValue());
                            assetBaseline.setContractor(contractor == null ? "" : contractor.getStringCellValue());
                            assetBaseline.setContractorPerson(contractorPerson == null ? "" : contractorPerson.getStringCellValue());
                            assetBaseline.setContractorPhone(contractorPhone == null ? "" : contractorPhone.getStringCellValue());
                            assetBaseline.setMaintainCompany(maintainCompany == null ? "" : maintainCompany.getStringCellValue());
                            assetBaseline.setMaintainPerson(maintainPerson == null ? "" : maintainPerson.getStringCellValue());
                            assetBaseline.setMaintainPhone(maintainPhone == null ? "" : maintainPhone.getStringCellValue());
                            assetBaseline.setMapCatalogueId(mapCatalogueId == null ? 0 : Integer.parseInt(mapCatalogueId.getStringCellValue()));
                            assetBaseline.setRemark(remark == null ? "" : remark.getStringCellValue());
                            assetBaseline.setCreator(userSession.getUserName());
                            assetBaseline.setReviser(userSession.getUserName());
                            assetBaseline.setCreateTime(DateHelper.getDateTime(new Date()));
                            assetBaseline.setUpdateTime(DateHelper.getDateTime(new Date()));

                            if (deviceType != null) {
                                for (DeviceType type : deviceTypes) {
                                    if (type.getTypeName().equals(deviceType.getStringCellValue())) {
                                        assetBaseline.setDeviceType(type.getTypeId());
                                    }
                                }
                            }
                            if (projectStatus != null) {
                                switch (projectStatus.getStringCellValue()) {
                                    case "建设中":
                                        assetBaseline.setProjectStatus(0);
                                        break;
                                    case "质保":
                                        assetBaseline.setProjectStatus(1);
                                        break;
                                    case "过保":
                                        assetBaseline.setProjectStatus(2);
                                        break;
                                    case "维保":
                                        assetBaseline.setProjectStatus(3);
                                        break;
                                }
                            }

                            assetBaseline.setBindStatus(bindStatus == null ? 0 : (bindStatus.getStringCellValue().equals("已绑定") ? 1 : 0));
                            entityList.add(assetBaseline);


                        } catch (Exception e) {
                            log.error("录入失败,错误：" + e.toString());
                            continue;
                        }
                    } else {
                        flag = false;//退出循环
                    }
                }
                for (AssetBaseline newAsset : entityList) {
                    boolean insert = false;
                    for (AssetBaseline baseline : assetBaselines) {
                        if (baseline.getIpAddr().equals(newAsset.getIpAddr())) {
                            newAsset.setId(baseline.getId());
                            insert = true;
                        }
                    }

                    if (insert) {
                        assetBaselineDao.updateByPrimaryKey(newAsset);
                    } else {
                        assetBaselineDao.insert(newAsset);
                    }
                }
                inputStream.close();
            } else {
                return ApiResult.error("检查模板是否正确");
            }
        } catch (IOException e) {
            log.error(e.toString());
            return ApiResult.error("解析文件失败,请检查上传文件格式");
        }
        return ApiResult.success("success");
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
    public ApiResult excelTransform(String filePath, UserSession userSession) {


        /**
         * @Author beixing
         * @Description excel根据模板将数据转换存储
         * 1.将导入的excel存储到本地；
         * 2.读取excel，得到excel的第一个Sheet的数据，
         * 并转成map数据,key为索引值，从零开始；
         * 3.根据模板配置的索引对应的key值和读取excel模板值进行映射匹配，然后将值set到实体类中；
         * 4.将实体类的值存储到基线表中；
         *
         * @Date 2021/9/14
         *
         **/

        String absoluteNewFilePath = this.uploadFolder + "/" + filePath;
        try {
            log.info("文件路径#{}", absoluteNewFilePath);
            EasyExcel.read(absoluteNewFilePath, new AssetDataListener()).sheet().doRead();
            return ApiResult.success();
        } catch (Exception ex) {
            log.error("读取资产上传文件失败#" + absoluteNewFilePath, ex);
            return ApiResult.error(SERVER_UPLOAD_ERROR, absoluteNewFilePath);
        }

    }


    private ExcelSheetReader getReader() {
        return ExcelSheetReader.builder()
                .headRowNumber(Lists.newArrayList(2))
                .sheetNo(0)
                .notNullFields(Lists.newArrayList("seq,ip_addr,device_model_des,point_name,pointName"))
                .headerClass(AssetBaselineExcelVO.class).build();
    }


    private ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader) throws Exception {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook, reader);
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
     *
     * @param poiSheet    poi 的sheet
     * @param sheetEntity 数据对象
     * @param writeExcel  true，校验结果写入excel； false，不写文件
     */
    private void checkInput(Sheet poiSheet, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
        boolean validResult = true;
        for (ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();

            Row poiRow = poiSheet.getRow(rowIndex);
            log.info("row[{}], {}", rowIndex, (poiRow != null));
            for (String name : excelRow.getData().keySet()) {
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

    @Override
    public int deleteIpByAlloc(Integer allocId) {
        return assetBaselineDao.deleteIpByAlloc(allocId);
    }
}
