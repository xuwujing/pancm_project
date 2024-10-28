package com.zans.base.office.validator;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xv
 * @since 2020/6/6 23:25
 */
@Slf4j
public class ValueInValidator implements IConstraintValidator {

    private List<Object> rangeValues;

    public ValueInValidator(String params) {
        if (params == null) {
            log.error("ValueInValidator params is null");
            this.rangeValues = null;
            return;
        }
        try {
            rangeValues = JSONObject.parseObject(params, new TypeReference<List<Object>>() {
            });
            log.info("rangeValues#{}", rangeValues);
        } catch (Exception ex) {
            log.error("ValueInValidator params error#"+params, ex);
        }
    }

    @Override
    public boolean isValid(Object var1) {
        if (this.rangeValues == null || this.rangeValues.size() == 0) {
            return true;
        }
        return rangeValues.contains(var1);
    }

    @Override
    public String getMessage() {
        return "取值不在允许范围内";
    }

}
