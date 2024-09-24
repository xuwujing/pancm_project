package com.pancm.test.excel.color;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description
 * @Date 2022/1/19
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class ExcelDemo {
    
    // 导出信息 为表头
    // 序号
    @ExcelProperty({"导出信息", "序号"})
    private String orderId;

    // 姓名
    @ExcelProperty({"导出信息", "姓名"})
    private String name;


    // 审核状态（待审核 0，未通过 1，已通过 2）
    @ExcelProperty({"导出信息", "审核状态"})
    private String status;

    /**
     * 指定单元格的样式。当然样式 也可以用注解等方式。
     *
     * @since 3.0.0-beta1
     */
    @ExcelIgnore
    private WriteCellData<String> writeCellStyle;
}