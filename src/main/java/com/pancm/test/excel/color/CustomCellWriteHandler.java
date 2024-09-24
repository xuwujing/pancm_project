package com.pancm.test.excel.color;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.write.handler.AbstractCellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;

public class CustomCellWriteHandler extends AbstractCellWriteHandler {

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        // 设置行高测试
        int rowIndex = row.getRowNum();
        System.out.println("当前行: " + rowIndex);
        short height = 600;
        row.setHeight(height);
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();

        // 自定义宽度处理

        // 自定义样式处理
        // 当前事件会在 数据设置到poi的cell里面才会回调
        // 判断不是头的情况 如果是fill 的情况 这里会==null 所以用not true
        if (BooleanUtils.isNotTrue(context.getHead())) {
            if ( cell.getStringCellValue().contains("已通过")) {
                // 拿到poi的workbook
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                // 不同单元格尽量传同一个 cellStyle
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyle);
                // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                // cell里面去 会导致自己设置的不一样（很关键）
                context.getFirstCellData().setWriteCellStyle(null);
            } else if (cell.getStringCellValue().contains("未通过")) {
                // 拿到poi的workbook
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                // 不同单元格尽量传同一个 cellStyle
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyle);
                // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                // cell里面去 会导致自己设置的不一样（很关键）
                context.getFirstCellData().setWriteCellStyle(null);
            } else if (cell.getStringCellValue().contains("待审核")) {
                // 拿到poi的workbook
                Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                // 这里千万记住 想办法能复用的地方把他缓存起来 一个表格最多创建6W个样式
                // 不同单元格尽量传同一个 cellStyle
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(cellStyle);
                // 由于这里没有指定dataformat 最后展示的数据 格式可能会不太正确
                // 这里要把 WriteCellData的样式清空， 不然后面还有一个拦截器 FillStyleCellWriteHandler 默认会将 WriteCellStyle 设置到
                // cell里面去 会导致自己设置的不一样
                context.getFirstCellData().setWriteCellStyle(null);
            }
        }
    }

}