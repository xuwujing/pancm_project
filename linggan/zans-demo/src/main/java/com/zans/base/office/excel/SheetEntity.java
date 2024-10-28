package com.zans.base.office.excel;

import com.zans.base.util.ObjectHelper;
import com.zans.base.util.StringHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/21 19:58
 */
@Data
@Slf4j
public class SheetEntity {

    private Sheet sheet;

    private Class headerClass;

    private List<Header> headers;

    private List<ExcelRow> data;

    private Map<String, String[]> ruleMap;

    private Boolean valid;

    public void resetRuleMap() {
        ruleMap = new HashMap<>(headers.size());
        for(Header header : headers) {
            String[] validates = header.getValidate();
            if (validates != null && validates.length > 0) {
                ruleMap.put(header.getFieldName(), validates);
            }
        }
    }

    public String[] getValidateRule(String key) {
        if (ruleMap == null || ruleMap.size() == 0) {
            return null;
        }
        return ruleMap.get(key);
    }

    public <T> List<T> convertToRawTable(Class<T> tClass) {
        if (this.headers == null || this.headers.size() == 0
                || this.data == null || this.data.size() == 0 || this.headerClass == null) {
            return null;
        }
        List<T> table = new LinkedList<>();
        String rowIndexField = "rowIndex";
        for (ExcelRow row : this.data) {
            try {
                T object = tClass.getDeclaredConstructor().newInstance();
                for (Header header : headers) {
                    String fieldName = header.getFieldName();
                    Object value = row.getColumnValue(fieldName);

                    Field field = tClass.getDeclaredField(fieldName);
                    if (field == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    String typeName = header.getType();
                    if ("Integer".equalsIgnoreCase(typeName)) {
                        Integer intVal = StringHelper.getIntValue(value);
                        if (intVal != null) {
                            field.set(object, intVal);
                        }
                    } else {
                        if (value != null && StringHelper.isNotBlank(value.toString())) {
                            field.set(object, value.toString());
                        }
                    }
                }

                ObjectHelper.setFiledValue(object, rowIndexField, row.getRow());
                table.add(object);

            } catch (Exception ex) {
                log.error("convert error#" + tClass, ex);
            }
        }
        return table;
    }
}
