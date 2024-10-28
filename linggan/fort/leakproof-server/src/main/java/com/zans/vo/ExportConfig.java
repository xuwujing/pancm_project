package com.zans.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author xv
 * @since 2020/4/30 16:02
 */
@Data
@Builder
public class ExportConfig {

    public static int TOP = 0;

    public static int BOTTOM = 1;
    /**
     * 首列是 序号
     */
    private boolean seqColumn;

    /**
     * 冻结单元格
     */
    private boolean freeze;

    /**
     * 冻结参数
     */
    private Integer freezeCol;

    /**
     * 冻结参数
     */
    private Integer freezeRow;

    /**
     * 自动换行
     */
    private boolean wrap;

    /**
     * 文本的位置
     * 0， top
     * 1，bottom
     */
    private Integer extraContentPosition;

    /**
     * 额外显示的内容
     */
    private Map<String, Object> extraContent;
    /**
     * 额外显示的内容内容下的 空白行
     */
    private int extraContentBlankRow=1;
    /**
     * 下拉选择框内容,ExcelProperty.isSelect = true,key==ExcelProperty.index+1
     */
    private Map<Integer, String[]> selectContent;
}
