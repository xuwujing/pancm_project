package com.zans.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class SelectVO {

    @JSONField(name = "key")
    private Integer itemKey;

    @JSONField(name = "value")
    private String itemValue;

    @JSONField(serialize = false)
    private String classType;


    public SelectVO() {
    }

    public Integer getItemKey() {
        return itemKey;
    }

    public void setItemKey(Integer itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}
