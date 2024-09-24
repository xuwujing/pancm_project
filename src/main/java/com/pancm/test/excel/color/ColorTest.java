package com.pancm.test.excel.color;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 导出
 */
public class ColorTest {


    public static void main(String[] args) {

        // 这里是个查询列表的语句
        List<ExcelDemo> list = new ArrayList<>();
        ExcelDemo excelDemo = new ExcelDemo();
        excelDemo.setOrderId("1");
        excelDemo.setName("张三");
        excelDemo.setStatus("未通过");
        list.add(excelDemo);
        excelDemo = new ExcelDemo();
        excelDemo.setOrderId("2");
        excelDemo.setName("李四");
        excelDemo.setStatus("已通过");
        list.add(excelDemo);
        excelDemo = new ExcelDemo();
        excelDemo.setOrderId("3");
        excelDemo.setName("王五");
        excelDemo.setStatus("待审核");
        list.add(excelDemo);
        // 当前用户桌面
//        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
//        String desktopPath = desktopDir.getAbsolutePath() + "\\导出信息.xlsx";
        String desktopPath = System.currentTimeMillis() + "导出信息.xlsx";
        EasyExcel.write(desktopPath, ExcelDemo.class)
                .inMemory(true) // 富文本
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 字体居中显示
                .registerWriteHandler(new CustomCellWriteHandler()) // 自定义的样式
                .sheet("导出信息")
                .doWrite(list);
    }

}
