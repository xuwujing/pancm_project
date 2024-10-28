package com.zans.base.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.util.StringHelper;
import lombok.Data;

import static com.zans.base.config.BaseConstants.CONSTANT_MODULE_TYPE_INT;
import static com.zans.base.config.BaseConstants.CONSTANT_MODULE_TYPE_STRING;

@Data
public class SelectVO {

    @JSONField(name = "key")
    private Object itemKey;

    @JSONField(name = "value")
    private String itemValue;

    @JSONField(serialize = false)
    private String classType;

    public SelectVO(Object itemKey, String itemValue) {
        this.itemKey = itemKey;
        this.itemValue = itemValue;
    }

    public SelectVO() {
    }

    public static SelectVO getAllSelect() {
        return new SelectVO("", "全部");
    }

    public void resetKey() {

        if (classType == null || classType.equalsIgnoreCase(CONSTANT_MODULE_TYPE_STRING)) {
            return;
        }
        if (classType.equalsIgnoreCase(CONSTANT_MODULE_TYPE_INT)) {
            Integer val = StringHelper.getIntValue(this.itemKey);
            if (val != null) {
                this.itemKey = val;
            }
        }
    }
}
