package com.zans.base.office.excel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author xv
 * @since 2020/3/21 11:32
 */
@Builder
@Data
public class ExcelSheetReader {

    /**
     * header 对应的行号，处理多行表头
     */
    private List<Integer> headRowNumber;

    /**
     * sheet 序号
     */
    private Integer sheetNo;

    /**
     * sheet name，以 name 优先
     */
    private String sheetName;

    /**
     * 对象列名
     */
    private List<Header> headers;

    /**
     * 表头的配置类，读取 annotation
     */
    private Class headerClass;

    /**
     * 内容不允许为 null or empty 的列
     * 避免无关字段干扰
     */
    private List<String> notNullFields;
}
