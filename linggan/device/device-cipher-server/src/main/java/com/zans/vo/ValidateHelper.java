package com.zans.vo;

import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xv
 * @since 2020/3/21 23:42
 */
@Slf4j
public class ValidateHelper {

    private static Map<String, IConstraintValidator> validatorMap;

    static {
        validatorMap = new ConcurrentHashMap<>(10);
        validatorMap.put("not_null", new NotNullValidator());
        validatorMap.put("not_empty", new NotEmptyValidator());
    }

    public static ValidateResult doValidate(Object input, String[] rules) {
        if (rules == null || rules.length == 0) {
            return ValidateResult.passed();
        }
        ValidateResult result = ValidateResult.builder().build();
        for(String rule : rules) {
            IConstraintValidator validator = getValidator(rule);
            if (validator == null) {
                continue;
            }
            boolean valid = validator.isValid(input);
            if (!valid) {
                result.addMessage(validator.getMessage());
            }
        }
        if (result.getMessageList() == null || result.getMessageList().size() == 0) {
            result.setPassed(true);
        } else {
            result.setPassed(false);
        }
        return result;
    }

    private static IConstraintValidator getValidator(String rule) {
        IConstraintValidator validator = validatorMap.get(rule);
        if (validator != null) {
            return validator;
        }
        try {
            String[] array = rule.split("#");
            String className = array[0];
            String params = array[1];
            log.info("class#{}", className);
            Class tClass = Class.forName(className);
            validator = (IConstraintValidator)tClass.getDeclaredConstructor(String.class).newInstance(params);
            validatorMap.put(rule, validator);
            return validator;
        } catch (Exception ex) {
            log.error("getValidator error#" + rule, ex);
        }
        return null;
    }
}
