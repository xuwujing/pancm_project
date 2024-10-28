package com.zans.vo.excel;

import com.zans.utils.StringHelper;
import com.zans.vo.Header;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.zans.constant.BaseConstants.EXCEL_ROW_TYPE_SKIP;

/**
 * @author xv
 * @since 2020/3/21 11:40
 */
@Data
public class ExcelRow {

    public static ExcelRow getSkipRow() {
        ExcelRow row = new ExcelRow();
        row.setRowType(EXCEL_ROW_TYPE_SKIP);
        return row;
    }

    /**
     * 行号
     */
    private Integer row;

    private Integer rowType;

    private Map<String, ExcelColumn> data;

    /**
     * 初始化
     * @param header
     * @param value
     */
    public void set(Header header, Object value) {
        if (data == null) {
            data = new LinkedHashMap<>();
        }
        String key = header.getFieldName();
        ExcelColumn column = ExcelColumn.builder().col(header.getCol()).name(key).value(value).build();
        this.data.put(key, column);
    }

    /**
     * 更改内存中的值
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (data == null) {
            return;
        }
        ExcelColumn column = this.data.get(key);
        if (column == null) {
            return;
        }
        column.setValue(value);
    }

    public ExcelColumn getColumn(String key) {
        return this.data.get(key);
    }

    public Object getColumnValue(String key) {
        ExcelColumn column = this.data.get(key);
        if (column == null) {
            return null;
        }
        return column.getValue();
    }

    /**
     * 验证是否时空行
     * @return
     */
    public boolean isEmpty() {
        if (this.data == null) {
            return false;
        }
        boolean isEmpty = true;
        for (String key : data.keySet()) {
            ExcelColumn column = data.get(key);
            Object value = column.getValue();
            if (value == null) {
                continue;
            }
            if (StringHelper.isNotBlank(value.toString())) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    @Override
    public String toString() {
        return "ExcelRow{" +
                "row=" + row +
                ", rowType=" + rowType +
                ", data=" + data +
                '}';
    }
}
