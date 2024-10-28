package com.zans.vo.excel;

import lombok.Builder;
import lombok.Data;

/**
 * @author xv
 * @since 2020/3/21 11:39
 */
@Builder
@Data
public class ExcelColumn {

    /**
     * 列序号
     */
    private Integer col;

    /**
     * 列的对象名
     */
    private String name;

    /**
     * 列的值
     */
    private Object value;

    private Boolean valid;

    private String errorMessage;

    @Override
    public String toString() {
        return "ExcelColumn{" +
                "col=" + col +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", valid=" + valid +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
