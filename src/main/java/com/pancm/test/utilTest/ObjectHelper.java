package com.pancm.test.utilTest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;

/**
 * @author xv
 * @since 2020/4/30 13:42
 */
@Slf4j
public class ObjectHelper {

    /**
     * 反射获得对象的属性值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFiledValue(Object obj, String fieldName) {
        try {
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
            return pd.getReadMethod().invoke(obj);
        } catch (Exception ex) {
            log.error("class[{}], field#{} getValue error", obj.getClass(), fieldName);
        }
        return null;
    }

    public static String getFieldStringValue(Object obj, String fieldName) {
        Object value = getFiledValue(obj, fieldName);
        return (value == null) ? "" : value.toString();
    }

    public static void setFiledValue(Object obj, String fieldName, Object val) {
        try {
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), fieldName);
            if (pd!=null) {
                pd.getWriteMethod().invoke(obj, val);
            }
        } catch (Exception ex) {
            log.error("class[{}], field#{}, val#{} setFiledValue error#{}", obj.getClass(), fieldName, val, ex);
        }
    }

}
