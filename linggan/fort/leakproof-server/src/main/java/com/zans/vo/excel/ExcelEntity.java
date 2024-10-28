package com.zans.vo.excel;

import com.zans.vo.SheetEntity;
import lombok.Data;

/**
 * @author xv
 * @since 2020/6/5 22:34
 */
@Data
public class ExcelEntity {

    protected SheetEntity entity;

    /**
     * 是否校验通过
     */
    protected Boolean valid;

    protected String fileName;
}
