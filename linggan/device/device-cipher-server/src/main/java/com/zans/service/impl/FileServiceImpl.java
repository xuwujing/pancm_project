package com.zans.service.impl;

import com.zans.service.IFileService;
import com.zans.utils.DateHelper;
import com.zans.utils.FileHelper;
import com.zans.utils.StringHelper;
import com.zans.vo.Header;
import com.zans.vo.*;
import com.zans.vo.excel.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.zans.vo.excel.ExcelHelper.DEFAULT_HEADER_SEQ;
import static com.zans.vo.ExportConfig.BOTTOM;
import static com.zans.vo.ExportConfig.TOP;


/**
 * @author xv
 * @since 2020/3/21 13:09
 */
@Service("fileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    private static final Pattern PATTERN = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");



    @Override
    public String generateFileByTemplate(String fileName, InputStream is, String outFilePath, Map<String, Object> valueMap) throws Exception {
        return null;
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





}
