package com.zans.base.office.excel;

import lombok.Builder;
import lombok.Data;

/**
 * @author xv
 * @since 2020/6/8 14:56
 */
@Data
@Builder
public class ExcelCell {

    private Integer row;

    private Integer column;
}
