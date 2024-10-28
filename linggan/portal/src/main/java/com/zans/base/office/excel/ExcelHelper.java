package com.zans.base.office.excel;

import com.google.common.collect.Lists;
import com.zans.base.office.annotation.ExcelProperty;
import com.zans.base.office.annotation.ExcelPropertyMatcher;
import com.zans.base.office.validator.ValidateHelper;
import com.zans.base.office.validator.ValidateResult;
import com.zans.base.util.FileHelper;
import com.zans.base.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zans.base.config.BaseConstants.*;

/**
 * @author xv
 * @since 2020/3/21 15:47
 */
@Slf4j
public class ExcelHelper {

    public static final String DEFAULT_HEADER_SEQ = "DEFAULT_HEADER_SEQ";

    /**
     * 判断扩展名是否是excel扩展名
     * @param extension
     * @return
     */
    public static Boolean checkExtension(String extension){
        if (extension == null) {
            return false;
        }
        return Lists.newArrayList("xls", "xlsx", "XLS", "XLSX").contains(extension);
    }

    /**
     * 判断扩展名是否是excel扩展名
     * @param file
     * @return
     */
    public static Boolean checkExtension(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        String extension = FileHelper.getFileExtension(fileName);
        if (extension == null) {
            return false;
        }
        return checkExtension(extension);
    }

    /**
     * 自动判断文件类型
     * @param file 上传文件
     * @return Workbook
     * @throws IOException IO异常
     */
    public static Workbook getWorkbookAuto(MultipartFile file) throws IOException {
        return getWorkbookAuto(file.getOriginalFilename(), file.getInputStream());
    }

    /**
     * 自动判断文件类型
     * @param fileName 文件名
     * @param is 文件流
     * @return Workbook
     * @throws IOException IO异常
     */
    public static Workbook getWorkbookAuto(String fileName, InputStream is) throws IOException {
        boolean isExcel2003 = true;
        if (isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(bis);
        } else {
            wb = new XSSFWorkbook(bis);
        }
        return wb;
    }

    /**
     * 判断上传的Excel版本是否是2003及以前的版本
     * @param filePath
     * @return
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 判断上传的Excel版本是否是2007及以后的版本
     * @param filePath
     * @return
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 将单元格内的值转换成正确的String类型字符串
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String cellValue = "";
        // 以下是判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC:
                // 数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            case STRING:
                // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                // 公式
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                cellValue = getCellValue(evaluator.evaluateInCell(cell));
                break;
            case BLANK:
                // 空值
                break;
            case ERROR:
                // 故障
                break;
            default:
                break;
        }

        if (cellValue != null) {
            cellValue = cellValue.trim();
        }
        return cellValue;
    }

    /**
     * 获取Excel中对应的Sheet
     * @param workbook
     * @param reader    根据reader中配置的sheetNo或者sheetName获取,sheetName优先级高于sheetNo
     * @return
     */
    public static Sheet getSheet(Workbook workbook, ExcelSheetReader reader) {
        try {
            Sheet sheet = null;
            if (reader.getSheetNo() != null) {
                sheet = workbook.getSheetAt(reader.getSheetNo());
            } else {
                sheet = workbook.getSheet(reader.getSheetName());
            }

            if (sheet == null) {
                log.error("get sheet error#{}", reader);
                return null;
            }
            return sheet;
        } catch (Exception ex) {
            log.error("getSheet error#" + reader, ex);
        }
        return null;
    }

    public static SheetEntity readExcelSheet(Sheet sheet, ExcelSheetReader reader) {
        try {

            if (sheet == null) {
                log.error("get sheet error#");
                return null;
            }

            SheetEntity excelEntity = new SheetEntity();
            excelEntity.setHeaderClass(reader.getHeaderClass());
            //从注解中获取校验相关
            List<Header> headers = getSheetHeader(sheet, reader);
            excelEntity.setHeaders(headers);


//            log.info("fix headers#{}", headers);
            List<Integer> rowNums = reader.getHeadRowNumber();
            int startIndex = 1;
            if (rowNums.size()>0){
                startIndex = rowNums.get(rowNums.size() -1 );
            }
//            log.info("data start from row#{}", startIndex);

            int count = 1;
            List<ExcelRow> dataList = new LinkedList<>();
            List<Integer> emptyRowList = new LinkedList<>();
            while (true) {
                ExcelRow obj = parseRow(sheet, startIndex, headers, reader.getNotNullFields());
                if (obj == null) {
                    emptyRowList.add(startIndex);
                } else {
                    emptyRowList.clear();
                }
                if (emptyRowList.size() > EXCEL_MAX_EMPTY_HEADER_COUNT || count > 10000) {
                    break;
                }

                if (obj != null) {
//                    log.info("{}#{}", count, obj);
                    dataList.add(obj);
                }
                startIndex ++;
                count ++;
            }
            excelEntity.setData(dataList);
            return excelEntity;
        } catch (Exception ex) {
            log.error("readIpAllocFile error", ex);
        }
        return null;
    }

    /**
     * 获得 Annotation 中的定义
     * @param clazz
     * @param ignore 是否忽略 注解中的 ignore = true
     * @param seqColumn 是否增加序号列
     * @return
     */
    public static List<Header> getHeadersFromClass(Class clazz, boolean ignore, boolean seqColumn) {
        List<Header> headers = new LinkedList<>();
        if (seqColumn) {
            Header seqHeader = Header.builder()
                    .fileName(new String[] {"序号"})
                    .width(-1)
                    .col(0)
                    .fieldName(DEFAULT_HEADER_SEQ)
                    .ignore(false)
                    .type("Integer").build();
            headers.add(seqHeader);
        }

        // 获得 Annotation 中的定义
        for(Field f : clazz.getDeclaredFields()) {
            String fieldName = f.getName();
            ExcelProperty[] properties = f.getAnnotationsByType(ExcelProperty.class);
            if (properties == null || properties.length == 0) {
                continue;
            }
            ExcelProperty ep = properties[0];
            if (ep.value() == null || "".equals(ep.value())) {
                log.error("error ExcelProperty of field#{}, {}", fieldName, ep);
                continue;
            }
//            log.info("ep#{}, {}, {}", ep.value(), ep.ignore(), ignore);
            if (ignore && ep.ignore()) {
                continue;
            }
            Header header = Header.builder()
                    .fileName(ep.value())
                    .width(ep.width())
                    .col(ep.index())
                    .fieldName(fieldName)
                    .ignore(ep.ignore())
                    .select(ep.isSelect())
                    .type(ep.type())
                    .colors(ep.colors())
                    .validate(ep.validate())
                    .dateFormat(ep.dateFormat())
                    .numFormat(ep.numFormat()).build();
            header.initColorMap();
            headers.add(header);
        }
        headers.sort(new HeaderComparator());
        return headers;
    }

    public static List<Header> getSheetHeader(Sheet sheet, ExcelSheetReader reader) {
        // 获得 Annotation 中的定义 加入到head中
        List<Header> headers = getHeadersFromClass(reader.getHeaderClass(), false, false);

        List<Integer> rowNumbers = reader.getHeadRowNumber();
        Map<Integer, Integer> rowHeaderCountMap = new TreeMap<>();
        for(Integer index : rowNumbers) {
            int rowCount = 0;
            Row row = sheet.getRow(index -1 );//获取表头行
            Map<String, Integer> map = getColumnMapOfRow(row);//表头的名字---列号
            for (Header header : headers) {
                String[] nameArray = header.getFileName();
                if (nameArray == null) {
                    log.error("error Header[{}] value is null" + header.getFieldName());
                    continue;
                }

                for (String matchRule : nameArray) {
                    String key = ExcelPropertyMatcher.getMatchKey(map.keySet(), matchRule);
                    if (key != null) {
                        Integer colIndex = map.get(key);
//                        log.info("field#{} - {}, {} - {}", header.getFieldName(), matchRule, key, colIndex);
                        header.setCol(colIndex);
                        rowCount ++;
                        break;
                    }
                }

            }
            rowHeaderCountMap.put(index, rowCount);
        }

        List<Integer> fixRowNumbers = new LinkedList<>();
        for(Integer index : rowHeaderCountMap.keySet()) {
            Integer count = rowHeaderCountMap.get(index);
            if ( count != null && count > 0) {
                fixRowNumbers.add(index);
            }
        }
        Collections.sort(fixRowNumbers);
        reader.setHeadRowNumber(fixRowNumbers);
//        log.info("fix rowNumbers#{}", fixRowNumbers);

        return headers;
    }

    /**
     * 检查文件,不通过写入错误文件 outFilePath
     * @param fileName
     * @param file
     * @param outFilePath
     * @param reader
     * @return
     * @throws Exception
     */
    public static ExcelEntity checkFile(String fileName, File file, String outFilePath, ExcelSheetReader reader)
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

            checkInput(workbook, sheetEntity, true, dataStyle);
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
    private static void checkInput( Workbook workbook, SheetEntity sheetEntity, boolean writeExcel, CellStyle cellStyle) {
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
    /**
     * 获得 每行的列数
     * 判断逻辑：从第一列开始，到第一个空列结束
     * @param row
     * @return
     */
    private static Map<String, Integer> getColumnMapOfRow(Row row) {
        Map<String, Integer> columnMap = new HashMap<>(20);
        if (row == null) {
            return columnMap;
        }
        int colNum = 0;
        List<Integer> emptyColList = new LinkedList<>();
        int maxEmptyColNum = EXCEL_MAX_EMPTY_HEADER_COUNT;
        while (emptyColList.size() < maxEmptyColNum) {
            String value = getCellValue(row.getCell(colNum));
            if (StringHelper.isEmpty(value)) {
                emptyColList.add(colNum);
            } else {
                columnMap.put(value, colNum);
                emptyColList.clear();
            }
            colNum++;
        }
        return columnMap;
    }

    private static ExcelRow parseRow(Sheet sheet, int rowIndex, List<Header> headers, List<String> notNullFields) throws Exception{
        // POI 中，边框不算空行
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return null;
        }
//        // 跳过设置背景颜色的行
//        if (row.getRowStyle() != null) {
//            log.info("row[{}] style#{}", rowIndex, row.getRowStyle().getFillBackgroundColor());
//            return ExcelRow.getSkipRow();
//        }
        ExcelRow rowResult = new ExcelRow();
        rowResult.setRow(rowIndex);

        int styleCol = 0;
        for (Header header : headers) {
//            log.info("header#{}, {}", header.getFieldName(), header.getCol());
            Integer colIndex = header.getCol();
            if (colIndex == null || colIndex < 0) {
                continue;
            }

            String fieldName = header.getFieldName();
            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                if (notNullFields != null && notNullFields.contains(fieldName)) {
                    rowResult = null;
                    break;
                } else {
                    rowResult.set(header, "");
                    continue;
                }
            }

            String strValue = getCellValue(cell);
            String typeName = header.getType();

            if ("Integer".equalsIgnoreCase(typeName)) {
                Integer intVal = StringHelper.getIntValue(strValue);
                if (intVal != null) {
                    rowResult.set(header, intVal);
                } else {
                    if (notNullFields != null && notNullFields.contains(fieldName)) {
                        rowResult = null;
                        break;
                    }
                }

            } else {
                if (StringHelper.isBlank(strValue)) {
                    if (notNullFields != null && notNullFields.contains(fieldName)) {
                        rowResult = null;
                        break;
                    } else {
                        rowResult.set(header, "");
                        continue;
                    }
                }
            }

            rowResult.set(header, strValue);
        }
        if (rowResult == null || rowResult.isEmpty()) {
            return null;
        } else {
            rowResult.setRowType(EXCEL_ROW_TYPE_NORMAL);
            return rowResult;
        }
    }

    public static CellStyle getDataCellStyle(Workbook workbook, boolean wrap) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(wrap);
        return style;
    }

    public static CellStyle getDataThinCellStyle(Workbook workbook, boolean wrap) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(wrap);
        return style;
    }

    public static String[] FILL_FOREGROUND_COLORS = {"red", "green"};

    public static Map<String, CellStyle> getDataColorCellStyleMap(Workbook workbook, boolean wrap) {
        Map<String, CellStyle> colorStyleMap = new HashMap<>(FILL_FOREGROUND_COLORS.length);
        for (String color : FILL_FOREGROUND_COLORS) {
            CellStyle style = getDataCellStyle(workbook,wrap);
            if ("red".equalsIgnoreCase(color)) {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            } else if ("green".equalsIgnoreCase(color)) {
                style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            colorStyleMap.put(color, style);
        }
        return colorStyleMap;
    }


    public static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = getDataCellStyle(workbook,false);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

}
