package com.zans.portal.service.impl;

import com.google.common.collect.Lists;
import com.zans.base.exception.IpAssignException;
import com.zans.base.office.excel.Header;
import com.zans.base.office.excel.*;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.util.*;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.IpAlloc;
import com.zans.portal.service.IAreaService;
import com.zans.portal.service.IDeviceTypeService;
import com.zans.portal.service.IFileService;
import com.zans.portal.service.IIpService;
import com.zans.portal.vo.file.IpAllocFile;
import com.zans.portal.vo.ip.ExcelIpAssignVO;
import com.zans.portal.vo.ip.IpAssign;
import com.zans.portal.vo.ip.IpVO;
import com.zans.portal.vo.network.LinkVO;
import com.zans.portal.vo.network.PointVO;
import com.zans.portal.vo.network.ProjectVO;
import com.zans.portal.vo.radius.QzViewRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zans.base.config.BaseConstants.*;
import static com.zans.base.office.excel.ExcelHelper.DEFAULT_HEADER_SEQ;
import static com.zans.base.office.excel.ExportConfig.BOTTOM;
import static com.zans.base.office.excel.ExportConfig.TOP;
import static com.zans.portal.config.GlobalConstants.DEVICE_TYPE_SERVER;
import static com.zans.portal.config.GlobalConstants.IP_ASSIGN_COLUMNS;

/**
 * @author xv
 * @since 2020/3/21 13:09
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Autowired
    IAreaService areaService;

    @Autowired
    IDeviceTypeService deviceTypeService;

    @Autowired
    IIpService ipService;


    private static final Pattern pattern = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");


    @Override
    public boolean validateAllocFile(String fileName) {
        return false;
    }

    @Override
    public IpAllocFile readIpAllocFile(String fileName, InputStream is) throws Exception {
        IpAllocFile file = new IpAllocFile();

        // 设置 文件名、区域、设备类型
        this.setAreaAndDeviceTypeByName(file, fileName);

        ExcelSheetReader reader = getIpReader();

        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity excelEntity = ExcelHelper.readExcelSheet(sheet, reader);
            file.setEntity(excelEntity);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close error", ex1);
            }
        }
        return file;
    }


    @Override
    public ExcelEntity readFile(String fileName, File file, ExcelSheetReader reader) throws Exception {
        ExcelEntity result = new ExcelEntity();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            Sheet sheet = ExcelHelper.getSheet(workbook, reader);
            SheetEntity excelEntity = ExcelHelper.readExcelSheet(sheet, reader);
            result.setEntity(excelEntity);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex1) {
                log.error("readFile error, close error", ex1);
            }
        }
        return result;
    }

    @Override
    public IpAllocFile checkFile(String fileName, InputStream is, String outFilePath) throws Exception {
        IpAllocFile file = new IpAllocFile();
        ExcelSheetReader reader = getIpReader();
        OutputStream os = null;
        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            this.checkInput(sheet, sheetEntity, true, dataStyle);
            file.setValid(sheetEntity.getValid());
            file.setEntity(sheetEntity);

            // 校验不通过，修改文件
            if (!file.getValid()) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
//                os.flush();
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
                log.error("readIpAllocFile error, close file error", ex1);
            }
        }
        return file;
    }

    @Override
    public IpAllocFile checkFileIp(String fileName, InputStream is, String outFilePath) throws Exception {
        IpAllocFile file = new IpAllocFile();
        ExcelSheetReader reader = getIpReader();
        OutputStream os = null;
        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;
            sheetEntity.resetRuleMap();

            this.checkInputIp(sheet, sheetEntity, true, dataStyle);
            file.setValid(sheetEntity.getValid());
            file.setEntity(sheetEntity);

            // 校验不通过，修改文件
            if (!file.getValid()) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
//                os.flush();
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
                log.error("readIpAllocFile error, close file error", ex1);
            }
        }
        return file;
    }

    @Override
    public ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader) throws Exception {
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

    private void setAreaAndDeviceTypeByName(IpAllocFile file, String fileName) {
        // 设置 文件名、区域、设备类型
        file.setFileName(fileName);
        List<SelectVO> deviceTypeList = deviceTypeService.findDeviceTypeToSelect();
        file.resetDeviceType(deviceTypeList);
    }

    /**
     * 默认的 IP读取模板
     * @return excel 配置模板
     */
    private ExcelSheetReader getIpReader() {
        return ExcelSheetReader.builder()
                .headRowNumber(Lists.newArrayList(3, 2, 1, 4, 5))
                .sheetNo(0)
                .headerClass(ExcelIpAssignVO.class).build();
    }

    private ExcelSheetReader getSheetReader(int no) {
        return ExcelSheetReader.builder()
                .sheetNo(no).build();
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

    /**
     * 校验 excel输入的ip，判断是否合法，直接保存校验结果到 poi 对象
     * @param poiSheet poi 的sheet
     * @param sheetEntity 数据对象
     * @param writeExcel true，校验结果写入excel； false，不写文件
     */
    private void checkInputIp(Sheet poiSheet, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
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
                //如果有值,则需要判断ip是在t_ip_all里面存在，如果存在，则不通过并进行进行提示
                if (result.getPassed()) {
                    if(!"ipAddr".equals(name)){
                        continue;
                    }
                    String ip = String.valueOf(val);
                    final boolean flag = isIp(ip);
                    if(!flag){
                        log.error("ip格式不符#ip:{}", ip);
                        List<String> stringList = new ArrayList<>();
                        stringList.add("该ip格式不符!");
                        result.setMessageList(stringList);
                    }else {
                        QzViewRespVO qzViewRespVO =ipService.findByIp(String.valueOf(val));
                        if(qzViewRespVO == null){
                            continue;
                        }
                        List<String> stringList = new ArrayList<>();
                        stringList.add("该ip已存在!");
                        result.setMessageList(stringList);
                    }
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
    public IpAllocFile assignIp(IpAlloc ipAlloc, String fileName, InputStream is, String outFilePath) throws Exception {
        IpAllocFile file = new IpAllocFile();

        ExcelSheetReader reader = getIpReader();
        OutputStream os = null;
        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;

            sheetEntity.resetRuleMap();

            this.checkInput(sheet, sheetEntity, true, dataStyle);
            file.setValid(sheetEntity.getValid());

            // 合法，分配IP
            if (file.getValid()) {
                // 设置 区域、设备类型
                if (ipAlloc != null) {
                    file.setArea(ipAlloc.getAreaId());
                    file.setDeviceType(ipAlloc.getDeviceType());
                } else {
                    this.setAreaAndDeviceTypeByName(file, fileName);
                }

                // 分配IP
                this.assignIp(sheetEntity, file.getArea(), file.getDeviceType());

                // 写入excel
                this.writeExcelSheet(sheet, sheetEntity, IP_ASSIGN_COLUMNS, dataStyle);
            }
            file.setEntity(sheetEntity);

            os = new FileOutputStream(new File(outFilePath));
            workbook.write(os);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close file error", ex1);
            }
        }
        return file;
    }

    /**
     * 手工分配 IP 只做存储，不做实际分配
     *
     * @param ipAlloc     有deviceType, area 信息
     * @param fileName    文件名
     * @param is          输入流
     * @param outFilePath 新文件的绝对路径
     * @return IP分配对象
     */
    @Override
    public IpAllocFile handAssignIp(IpAlloc ipAlloc, String fileName, InputStream is, String outFilePath) throws Exception {
        IpAllocFile file = new IpAllocFile();

        ExcelSheetReader reader = getIpReader();
        OutputStream os = null;
        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, true);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;

            sheetEntity.resetRuleMap();

            this.checkInput(sheet, sheetEntity, true, dataStyle);
            file.setValid(sheetEntity.getValid());

            // 合法，分配IP
            if (file.getValid()) {
                // 设置 区域、设备类型
                if (ipAlloc != null) {
                    file.setArea(ipAlloc.getAreaId());
                    file.setDeviceType(ipAlloc.getDeviceType());
                } else {
                    this.setAreaAndDeviceTypeByName(file, fileName);
                }

                // 分配IP
//                this.assignIp(sheetEntity, file.getArea(), file.getDeviceType());

                // 写入excel
//                this.writeExcelSheet(sheet, sheetEntity, IP_ASSIGN_COLUMNS, dataStyle);
            }
            file.setEntity(sheetEntity);
            os = new FileOutputStream(new File(outFilePath));
            workbook.write(os);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("readIpAllocFile error, close file error", ex1);
            }
        }
        return file;
    }



    private void writeExcelSheet(Sheet poiSheet, SheetEntity sheetEntity, String[] properties, CellStyle cellStyle) {

        for(ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();
            Row poiRow = poiSheet.getRow(rowIndex);
            for(String name : properties) {
                ExcelColumn column = excelRow.getColumn(name);
                if (column == null) {
                    log.error("column name#{}", name);
                    continue;
                }
                Integer colIndex = column.getCol();
                Object val = column.getValue();
                if (val == null || colIndex == null || colIndex < 0) {
                    log.error("column name#{}, index#{}", name, colIndex);
                    continue;
                }
                Cell poiCell = poiRow.getCell(colIndex);
                if (poiCell == null) {
                    poiCell = poiRow.createCell(colIndex);
                    poiCell.setCellStyle(cellStyle);
                }
                poiCell.setCellValue(val.toString());
            }
        }
    }

    /**
     * 分配IP
     * @param sheetEntity sheet
     * @param area 区域编号
     * @param deviceType 设备类型编号
     */
    private void assignIp(SheetEntity sheetEntity, Integer area, Integer deviceType) {

        IpVO lastIp = ipService.findLastAssignIp(area, deviceType);
        if (lastIp == null) {

            lastIp = ipService.findDefaultAssignIp(area, deviceType);
            if (lastIp == null) {
                // TODO，获得区域、设备的原始配置信息
                String errorMessage = String.format("not found ip in area#%d, deviceType#%d", area, deviceType);
                log.error(errorMessage);
                throw new IpAssignException(errorMessage);
            }
        }

        // 校验IP格式
        this.checkIpFormat(lastIp);

        log.info("lastIp#{}-{}, {}, {}, {}, {}", area, deviceType, lastIp.getIpAddr(), lastIp.getMask(), lastIp.getVlan(), lastIp.getGateway());

        Map<Integer, ExcelRow> rowMap = new HashMap<>(sheetEntity.getData().size());
        List<IpAssign> ipAssignList = this.extractExcelToIpAssign(sheetEntity, rowMap);
        if (ipAssignList.size() == 0) {
            return;
        }

        List<List<IpAssign>> changeList = this.splitListByPointChange(ipAssignList);
        log.info("   total#{}, slice#{}", sheetEntity.getData().size(), changeList.size());


        IpAssign next = IpAssign.builder()
                .ip(lastIp.getIpAddr())
                .mask(lastIp.getMask())
                .vlan(StringHelper.getIntValue(lastIp.getVlan()))
                .gateway(lastIp.getGateway()).build();
        for (List<IpAssign> pointList : changeList) {
            String pointName = pointList.get(0).getPointName();
            log.info("begin to assign#{}, {}, {}", pointName, pointList.size(), next);
            next = this.assignIp(next, pointName, pointList, deviceType);
        }

        // 回写 SheetAssign
        for (List<IpAssign> pointList : changeList) {

            for(IpAssign ipAssign : pointList) {
                log.info("assing result#{}", ipAssign);
                Integer rowIndex = ipAssign.getRow();
                ExcelRow excelRow = rowMap.get(rowIndex);
                if (excelRow != null) {
                    excelRow.put("ipAddr", ipAssign.getIp());
                    excelRow.put("gateway", ipAssign.getGateway());
                    excelRow.put("mask", ipAssign.getMask());
                    excelRow.put("vlan", ipAssign.getVlan());
                }
                log.info("after , {}, {}", rowIndex, excelRow);
            }
        }

    }


    /**
     * 校验 ip，vlan，gateway，mask 的数据格式
     * @param lastIp
     */
    private void checkIpFormat(IpVO lastIp) {
        if (lastIp == null) {
            return;
        }

        Integer id = lastIp.getId();
        Map<String, Object> validateMap = MapBuilder.getBuilder()
                .put("ip", lastIp.getIpAddr())
                .put("mask", lastIp.getMask())
                .put("gateway", lastIp.getGateway()).build();
        List<Object> errorList = new ArrayList<>(validateMap.size());
        for(String name : validateMap.keySet()) {
            Object value = validateMap.get(name);
            if (value == null) {
                String errorMessage = String.format("%d.%s format error, null", id, name);
                errorList.add(errorMessage);
                continue;
            }
            if (!StringHelper.isValidIp(value.toString())) {
                String errorMessage = String.format("%d.%s format error#%s", id, name, value.toString());
                errorList.add(errorMessage);
            }
        }

        Integer lastVlan = StringHelper.getIntValue(lastIp.getVlan());
        if (lastVlan == null) {
            String errorMessage = String.format("%d.vlan is not int#%s", id, lastIp.getVlan());
            errorList.add(errorMessage);
        }
        if (errorList.size() > 0) {
            String errors = StringHelper.joinCollection(errorList, SEPARATOR_COMMA);
            log.error(errors);
            throw new IpAssignException(errors);
        }
    }

    /**
     * 相邻的 pointName变化，更改
     * @param ipAssignList 原始 IpAssign 列表
     * @return 分片的列表
     */
    private List<List<IpAssign>> splitListByPointChange(List<IpAssign> ipAssignList) {

        List<List<IpAssign>> changeList = new LinkedList<>();
        if (ipAssignList.size() == 0) {
            return changeList;
        }

        List<IpAssign> partList = new LinkedList<>();
        IpAssign privous = ipAssignList.get(0);
        partList.add(privous);
        if (ipAssignList.size() == 1) {
            changeList.add(partList);
            return changeList;
        }

        for (int i = 1; i < ipAssignList.size(); i++) {
            IpAssign previous = ipAssignList.get(i - 1);
            IpAssign current = ipAssignList.get(i);
            if (!previous.getPointName().equals(current.getPointName())) {
                changeList.add(partList);
                partList = new LinkedList<>();
            }
            partList.add(current);
        }
        changeList.add(partList);
        return changeList;
    }

    /**
     * 遍历Excel
     * @param sheetEntity excel数据集合
     * @param rowMap 建立 行号数据的索引
     * @return IP数组
     */
    private List<IpAssign> extractExcelToIpAssign(SheetEntity sheetEntity, Map<Integer, ExcelRow> rowMap) {
        // 生成排序的数据结构，再重新关联
        List<IpAssign> ipList = new LinkedList<>();

        for(ExcelRow excelRow : sheetEntity.getData()) {
            Integer rowIndex = excelRow.getRow();
            rowMap.put(rowIndex, excelRow);
            String pointName = (String)excelRow.getColumnValue("pointName");
            // fatal error，停止分配
            if (StringHelper.isBlank(pointName)) {
                String error = String.format("row#%d pointName is null", rowIndex);
                log.error(error);
                sheetEntity.setValid(false);
                throw new IpAssignException(error);
            }
            ipList.add(IpAssign.builder().row(rowIndex).pointName(pointName).build());
        }
        return ipList;
    }

    private IpAssign assignIp(IpAssign start, String pointName, List<IpAssign> ipList, Integer deviceType) {
        log.info("assignIp, {}, {}, {}, {}", start, pointName, ipList.size(), deviceType);
        int ipLast = Integer.parseInt(start.getIp().split(IP_SPLIT_SEPARATOR)[3]);
        int vlanLast =  Integer.parseInt(start.getGateway().split(IP_SPLIT_SEPARATOR)[3]);
        int leftIpCount;
        int assingCount = ipList.size();
        IpAssign nextAssign = start;

        boolean isServer = isServer(deviceType);
        // 去掉全0、全1 共两个地址；内场服务器保留2个，外场设备保留5个
        if (isServer) {
            leftIpCount = vlanLast - ipLast - 2 - 2;
        } else {
            leftIpCount = vlanLast - ipLast - 2 - 5;
        }
        log.info("start[{}] ip#{}, {}, {}, {}, {}", pointName, start, vlanLast, ipLast, leftIpCount, assingCount);
        if (assingCount > leftIpCount) {
            // 从下一 vlan 开始
            nextAssign = this.getNextVlan(start, isServer);
            log.info("{}, next vlan#{}, {}", ipLast, nextAssign.getIp(), isServer);
        }
        for(int i = 0; i < ipList.size(); i++) {
            IpAssign toAssign = ipList.get(i);
            Integer rowIndex = toAssign.getRow();
            String nextIp = StringHelper.ipIncrease(nextAssign.getIp());
            if (nextIp == null) {
                throw new IpAssignException(String.format("point[%s].%d, nextIp error#%s",
                        pointName, i, nextAssign.getIp()));
            }
            BeanUtils.copyProperties(nextAssign, toAssign);
            toAssign.setIp(nextIp);
            toAssign.setRow(rowIndex);
            nextAssign.setIp(nextIp);
            log.info("{}.{}, {}", pointName, i+1, toAssign.getIp());
            if (this.needToChangeVlan(nextIp, isServer)) {
                nextAssign = this.getNextVlan(nextAssign, isServer);
                log.info("ip#{}, need to change vlan#{}, {}", nextIp, nextAssign.getIp(), isServer);
            }
        }

        return nextAssign;
    }

    private boolean isServer(Integer deviceType) {
        return deviceType != null && deviceType == DEVICE_TYPE_SERVER;
    }

    private static int[][] SERVER_IP_RANGE = {
            {0, 30, 32, 62},
            {32, 62, 64, 94},
            {64, 94, 96, 126},
            {96, 126, 128, 158},
            {128, 158, 160, 190},
            {160, 190, 192, 222},
            {192, 222, 224, 254},
            {224, 254, 0, 30}
    };

    private static int[][] DEVICE_IP_RANGE = {
            {0, 62, 64, 126},
            {64, 126, 128, 190},
            {128, 190, 192, 254},
            {192, 254, 0, 62}
    };

    private boolean needToChangeVlan(String ipInput, boolean isServer) {
        if (ipInput == null) {
            return false;
        }
        String[] ipArray = ipInput.split(IP_SPLIT_SEPARATOR);
        int ipEnd = Integer.parseInt(ipArray[3]);

        if (ipEnd <= 0 || ipEnd >= IP_ASSIGN_LAST) {
            return true;
        }

        int reversed = (isServer) ? 2 : 5;
        int[][] range = (isServer) ? SERVER_IP_RANGE : DEVICE_IP_RANGE;

        for(int[] setting : range) {
            if (ipEnd > setting[0] && ipEnd < setting[1]) {
                if (ipEnd >= setting[1] - reversed) {
                    log.info("needToChangeVlan, ipEnd#{}, {}", ipEnd, setting);
                    return true;
                }
            }
        }
        return false;
    }

    private IpAssign getNextVlan(IpAssign input, boolean isServer) {
        IpAssign next = null;

        String nextMask = isServer ? "255.255.255.224" : "255.255.255.192";
        String[] ipArray = input.getIp().split(IP_SPLIT_SEPARATOR);
        String[] gwArray = input.getGateway().split(IP_SPLIT_SEPARATOR);
        int ipEnd = Integer.parseInt(ipArray[3]);
        int[][] ipRange = (isServer) ? SERVER_IP_RANGE : DEVICE_IP_RANGE;
        for(int[] setting : ipRange) {
            next = this.changeToNextVlan(ipEnd, ipArray, gwArray, setting);
            if (next != null) {
                break;
            }
        }

        if (next == null) {
            throw new IpAssignException("input#" + input.toString());
        }
        next.setVlan(input.getVlan() + 1);
        next.setMask(nextMask);
        next.setPointName(input.getPointName());
        return next;
    }

    private IpAssign changeToNextVlan(int ipEnd, String[] ipArray, String[] gwArray, int[] setting) {
        String nextIp;
        String nextGateway;
        if (ipEnd > setting[0] && ipEnd < setting[1]) {
            if (setting[1] != IP_ASSIGN_LAST ) {
                nextIp = String.format("%s.%s.%s.%d", ipArray[0], ipArray[1], ipArray[2], setting[2]);
                nextGateway = String.format("%s.%s.%s.%d", gwArray[0], gwArray[1], gwArray[2], setting[3]);
            } else {
                int ipClassC = Integer.parseInt(ipArray[2]) + 1;
                nextIp = String.format("%s.%s.%d.%d", ipArray[0], ipArray[1], ipClassC, setting[2]);
                int gwClassC = Integer.parseInt(gwArray[2]) + 1;
                nextGateway = String.format("%s.%s.%d.%d", gwArray[0], gwArray[1], gwClassC, setting[3]);
            }
            return IpAssign.builder().ip(nextIp).gateway(nextGateway).build();
        }
        return null;
    }

    @Override
    public String generateFileByTemplate(String fileName, InputStream is, String outFilePath, Map<String, Object> valueMap) throws Exception {
        IpAllocFile file = new IpAllocFile();
        ExcelSheetReader reader = getIpReader();
        OutputStream os = null;
        try {
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            Sheet sheet = ExcelHelper.getSheet(workbook,reader);
            SheetEntity sheetEntity = ExcelHelper.readExcelSheet(sheet, reader);
            is.close();
            is = null;

            boolean result = this.writeExcelByValueMap(sheet, sheetEntity, valueMap);

            // 校验不通过，修改文件
            if (result) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
                return outFilePath;
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
                log.error("readIpAllocFile error, close file error", ex1);
            }
        }
        return null;
    }

    private boolean writeExcelByValueMap(Sheet poiSheet, SheetEntity sheetEntity, Map<String, Object> valueMap) {
        if (sheetEntity == null || poiSheet == null || valueMap == null) {
            return false;
        }
        for(ExcelRow excelRow : sheetEntity.getData()) {
            int rowIndex = excelRow.getRow();
            Row poiRow = poiSheet.getRow(rowIndex);
            for (String name : excelRow.getData().keySet()) {
                ExcelColumn column = excelRow.getColumn(name);
                int colIndex = column.getCol();
                if (!valueMap.containsKey(name)) {
                    continue;
                }
                // 空值，也写
                Object newValue = valueMap.get(name);
                column.setValue(newValue);

                Cell poiCell = poiRow.getCell(colIndex);
                if (poiCell == null) {
                    continue;
                }
                if (newValue == null) {
                    poiCell.setBlank();
                } else {
                    poiCell.setCellValue(newValue.toString());
                }
            }
        }

        return true;
    }

    @Override
    public String generateFileByTemplate(String fileName, File file, String outFilePath, Map<ExcelCell, Object> valueMap) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            Sheet sheet = ExcelHelper.getSheet(workbook, getSheetReader(0));
            CellStyle style = ExcelHelper.getDataThinCellStyle(workbook, true);
            is.close();
            is = null;

            boolean result = this.writeExcelByValueMap(sheet, valueMap, style);

            // 校验不通过，修改文件
            if (result) {
                os = new FileOutputStream(new File(outFilePath));
                workbook.write(os);
                return outFilePath;
            }
        } catch (Exception ex) {
            String errorMessage = "设置模板失败#" + fileName;
            log.error(errorMessage, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("generateFileByTemplate error, close error", ex1);
            }
        }
        return null;
    }

    private boolean writeExcelByValueMap(Sheet poiSheet, Map<ExcelCell, Object> valueMap, CellStyle cellStyle) {
        if (poiSheet == null || valueMap == null) {
            return false;
        }
        for(ExcelCell cell : valueMap.keySet()) {
            Object value = valueMap.get(cell);
            Row poiRow = poiSheet.getRow(cell.getRow());
            if (poiRow == null) {
                log.error("cell is null#{}, {}", cell.getRow(), cell.getColumn());
                continue;
            }
            Cell poiCell = poiRow.getCell(cell.getColumn());
            if (poiCell == null) {
                poiCell = poiRow.createCell(cell.getColumn());
            }
            if (cellStyle != null) {
                poiCell.setCellStyle(cellStyle);
            }
            if (value == null) {
                poiCell.setBlank();
            } else {
                poiCell.setCellValue(value.toString());
            }
        }
        return true;
    }

    @Override
    public String generateFile(String fileName, File file, String outFilePath, ProjectVO project, ExportConfig exportConfig) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file);
            Workbook workbook = ExcelHelper.getWorkbookAuto(fileName, is);
            Sheet sheet = ExcelHelper.getSheet(workbook, getSheetReader(0));
            CellStyle cellStyle = ExcelHelper.getDataThinCellStyle(workbook, true);
            is.close();
            is = null;
            this.writeProject(sheet, project, 3);
            this.writeExcelByValueMap(sheet, project.toExcel(project.getPointCount()), cellStyle);


            os = new FileOutputStream(new File(outFilePath));
            workbook.write(os);
            return outFilePath;

        } catch (Exception ex) {
            String errorMessage = "生成文件失败#" + fileName;
            log.error(errorMessage, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception ex1) {
                log.error("generateFileByTemplate error, close error", ex1);
            }
        }
        return null;
    }

    private void writeProject(Sheet poiSheet, ProjectVO project, int start) {
        Row headerRow = poiSheet.getRow(start - 1);
        CellStyle style = headerRow.getCell(0).getCellStyle();
        int insertIndex = start;

        for (int i=0; i < project.getPointCount(); i++) {
            insertIndex = start + i;
            int lastRowNo = poiSheet.getLastRowNum();
            Row row = poiSheet.createRow(insertIndex);
        }
        insertIndex++;
        Row last = poiSheet.createRow(insertIndex);
        setRegionAndStyle(poiSheet, new CellRangeAddress(insertIndex, insertIndex, 0, 2), style);
        setRegionAndStyle(poiSheet, new CellRangeAddress(insertIndex, insertIndex, 3, 5), style);
        setRegionAndStyle(poiSheet, new CellRangeAddress(insertIndex, insertIndex, 6, 7), style);
        setRegionAndStyle(poiSheet, new CellRangeAddress(insertIndex, insertIndex, 8, 10), style);

        List<Header> linkHeaders = ExcelHelper.getHeadersFromClass(LinkVO.class, true, false);
        List<Header> pointHeaders = ExcelHelper.getHeadersFromClass(PointVO.class, true, false);

        project.resetRowIndexOfLinkList(start);

        for(LinkVO link : project.getLinkList()) {
            if (link.getPointList().size() > 1) {
                for (int i = 0; i < 8; i++) {
                    setRegionAndStyle(poiSheet, new CellRangeAddress(link.getRowIndex(), link.getEndIndex(), i, i), style);
                }
            }
            this.fillRowWithData(poiSheet, link.getRowIndex(), link, linkHeaders, style);

            for(PointVO point : link.getPointList()) {
                this.fillRowWithData(poiSheet, point.getRowIndex(), point, pointHeaders, style);
            }
        }

    }

    private void fillRowWithData(Sheet poiSheet, int rowIndex, Object data, List<Header> headers, CellStyle style) {
        Row dataRow = poiSheet.getRow(rowIndex);
        if (dataRow == null) {
            dataRow = poiSheet.createRow(rowIndex);
        }
        for (int j = 0; j < headers.size(); j++) {
            Header header = headers.get(j);
            String type = header.getType();
            String filedName = header.getFieldName();
            int col = header.getCol();
            Cell cell = dataRow.createCell(col);
            String value = "";
            if ("Date".equalsIgnoreCase(type)) {
                Object obj = ObjectHelper.getFiledValue(data, filedName);
                value = DateHelper.formatDate((Date)obj, header.getDateFormat());
            } else {
                value = ObjectHelper.getFieldStringValue(data, filedName);
            }
            cell.setCellStyle(style);
            cell.setCellValue(value);
        }
    }

    private void setRegionAndStyle(Sheet sheet, CellRangeAddress region , CellStyle cs) {
        sheet.addMergedRegion(region);
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                }
                cell.setCellStyle(cs);
            }
        }
    }


    @Override
    public boolean exportExcelFile(List data, String sheetName, String filePath, ExportConfig exportConfig) {
        if (data == null || data.size() == 0) {
            return false;
        }
        if (filePath == null) {
            return false;
        }
        log.info("export#{}, size#{}", filePath, data.size());
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(filePath);

            boolean seqColumn = false;
            boolean wrap = false;
            boolean freeze = false;
            Integer position = null;
            Map<String, Object> extraMap = null;
            if (exportConfig != null) {
                seqColumn = exportConfig.isSeqColumn();
                wrap = exportConfig.isWrap();
                freeze = exportConfig.isFreeze();
                position = exportConfig.getExtraContentPosition();
                extraMap = exportConfig.getExtraContent();
                if (extraMap == null || extraMap.size() == 0) {
                    position = null;
                }
            }

            // 获得 Annotation 中的定义
            List<Header> headers = ExcelHelper.getHeadersFromClass(data.get(0).getClass(), true, seqColumn);
            log.info("headers#{}, size#{}", headers, headers.size());
//            XSSFWorkbook workbook = new XSSFWorkbook();
            //改为SXSSFWorkbook  大文件下载更快
            SXSSFWorkbook workbook = new SXSSFWorkbook(200);
            CellStyle headerStyle = ExcelHelper.getHeaderCellStyle(workbook);
            CellStyle dataStyle = ExcelHelper.getDataCellStyle(workbook, wrap);

            // 避免频繁创建 cellStyle， POI 有数量限制
            Map<String, CellStyle> colorStyleMap = ExcelHelper.getDataColorCellStyleMap(workbook, wrap);

            Sheet sheet = workbook.createSheet(sheetName);
            if (freeze) {
                sheet.createFreezePane(exportConfig.getFreezeCol() , exportConfig.getFreezeRow());
            }


            for (int i = 0; i < headers.size(); i++) {
                Header header = headers.get(i);
                if (header.getWidth() == null || header.getWidth() < 0) {
                  //  sheet.autoSizeColumn(i);
                } else {
                    int width = header.getWidth() < 10 ? 10 : header.getWidth();
                    if (width > 254) {
                        // poi api 限制
                        width = 254;
                    }
                    sheet.setColumnWidth(i, width * 256);
                }
            }

            int rowIndex = 0;
            if (position != null && position == TOP) {
                int i = 0;
                this.createExtraRows(extraMap, 0, sheet, headerStyle, dataStyle);
                rowIndex = extraMap.size() + exportConfig.getExtraContentBlankRow();
            }

            Row rowHeader = sheet.createRow(rowIndex);
            for (int col = 0; col < headers.size(); col++) {
                Header header = headers.get(col);
                Cell cell = rowHeader.createCell(col);
                String headValue = header.getFileName()[0];
                if (header.isValidateNotEmpty()){
                    headValue+="*";
//                    XSSFRichTextString richString = new XSSFRichTextString(  headValue+"*" );
//                    richString.applyFont( headValue.length()-1, headValue.length(), redFont);
//                    cell.setCellStyle(headerStyle);
//                    cell.setCellValue(headValue);
                }
                cell.setCellStyle(headerStyle);
                cell.setCellValue(headValue);
            }

            for(int i=0; i < data.size(); i++) {
                Object rowData = data.get(i);
                rowIndex++;

                Row dataRow = sheet.createRow(rowIndex);
                for (int col = 0; col < headers.size(); col++) {
                    Header header = headers.get(col);
                    String type = header.getType();
                    String filedName = header.getFieldName();
                    Cell cell = dataRow.createCell(col);
                    String value = "";
                    // 序号列
                    if (DEFAULT_HEADER_SEQ.equals(filedName)) {
                        value = String.valueOf(i + 1);
                    } else {
                        if ("Date".equalsIgnoreCase(type)) {
                            Object obj = ObjectHelper.getFiledValue(rowData, filedName);
                            value = DateHelper.formatDate((Date)obj, header.getDateFormat());
                        } else {
                            value = ObjectHelper.getFieldStringValue(rowData, filedName);
                        }
                    }
                    String color = header.getColor(value);
                    CellStyle colorStyle = colorStyleMap.get(color);
                    if (colorStyle == null) {
                        colorStyle = dataStyle;
                    }
                    cell.setCellStyle(colorStyle);
                    cell.setCellValue(value);
                }
            }
            for (int i = 0; i < headers.size(); i++) {
                Header header = headers.get(i);
                if (header.getSelect() != null && header.getSelect()) {
                    addValidationToSheet(sheet,exportConfig.getSelectContent().get(i), i,1,rowIndex);
                }
            }
            if (position != null && position == BOTTOM) {
                this.createExtraRows(extraMap, rowIndex + 2, sheet, headerStyle, dataStyle);
            }

            workbook.write(output);//写入磁盘
        } catch (Exception ex) {
            log.error("save workbook error#"+ filePath, ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception ex1) {
                log.error("close output error#"+ filePath, ex1);
            }
        }


        return false;
    }

    /**
     * 给sheet页，添加下拉列表
     * @param targetSheet 级联列表所在sheet页
     * @param options     级联数据 ['百度','阿里巴巴']
     * @param column      下拉列表所在列 从'A'开始
     * @param fromRow     下拉限制开始行
     * @param endRow      下拉限制结束行
     */
    public static void addValidationToSheet(Sheet targetSheet, String[] options, int column, int fromRow, int endRow) {
        DataValidationHelper helper = targetSheet.getDataValidationHelper();
        // 生成下拉列表
        // 设置范围
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, endRow, column, column);
        // 生成下拉框内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(options);
        // 绑定下拉框和作用区域
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        //处理Excel兼容性问题
        if(dataValidation instanceof XSSFDataValidation){
            //数据校验
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }else{
            dataValidation.setSuppressDropDownArrow(false);
        }
        // 对sheet页生效
        targetSheet.addValidationData(dataValidation);

    }

    private void createExtraRows(Map<String, Object> extraMap, int rowIndex,
                                 Sheet sheet, CellStyle headerStyle, CellStyle dataStyle) {
        int row = rowIndex;
        for(String key : extraMap.keySet()) {
            Object value = extraMap.get(key);
            String strValue = (value == null) ? "" : value.toString();

            Row rowContent = sheet.createRow(row);
            Cell cellKey = rowContent.createCell(0);
            cellKey.setCellStyle(headerStyle);
            cellKey.setCellValue(key);

            Cell cellValue = rowContent.createCell(1);
            cellValue.setCellStyle(dataStyle);
            cellValue.setCellValue(strValue);
            row++;
        }
    }

    @Override
    public String getNewFileName(String fileName) {
        String extension = FileHelper.getFileExtension(fileName);
        if (extension == null) {
            extension = "xlsx";
        }
        return DateHelper.getToday() + "_" + StringHelper.getUuid() + "." + extension;
    }



    private  boolean isIp(String str) {
        Matcher m = pattern.matcher(str);
        return m.find();
    }

}
