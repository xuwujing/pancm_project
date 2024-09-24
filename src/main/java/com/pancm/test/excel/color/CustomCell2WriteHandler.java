package com.pancm.test.excel.color;


import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCell2WriteHandler implements CellWriteHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCell2WriteHandler.class);


    @Override
    public void afterCellCreate(CellWriteHandlerContext context) {

        Cell cell = context.getCell();
        // 获取当前行
        int rowIdx = cell.getRowIndex();
        // 获取当前列
        int colIdx = cell.getColumnIndex();
        if(!context.getHead()){
            if( !cell.getStringCellValue().contains("正常")){
                cell.getStringCellValue().contains("正常");
            }

        }

        // 设置单元格背景色
        cell.getCellStyle().setFillForegroundColor(IndexedColors.RED.getIndex());
        cell.getCellStyle().setFillPattern(FillPatternType.SOLID_FOREGROUND);


    }
}