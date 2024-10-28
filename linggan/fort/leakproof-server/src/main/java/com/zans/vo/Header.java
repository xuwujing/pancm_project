package com.zans.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/21 11:43
 */
@Data
@Builder
public class Header {

    /**
     * 序号
     */
    private Integer col;

    /**
     * header 的文件名
     */
    private String[] fileName;

    /**
     * header 的宽度
     */
    private Integer width;

    /**
     * header 的对象名
     */
    private String fieldName;

    private Boolean ignore;
    /**
     * 下拉框
     */
    private Boolean select;

    private String dateFormat;

    private String numFormat;

    private String type;

    private String[] validate;

    private String[] colors;

    private Map<String, String> colorMap;

    public Header initColorMap() {
        if (colorMap == null) {
            colorMap = new HashMap<>(2);
        }
        if (colors == null || colors.length == 0) {
            return this;
        }
        for (int i=0; i<colors.length; i=i+2) {
            if ( i + 1 >= colors.length) {
                colorMap.put(colors[i], "");
            } else {
                colorMap.put(colors[i], colors[i + 1]);
            }
        }
        return this;
    }
    public boolean isValidateNotEmpty(){
        if (validate != null && validate.length>0){
           return Arrays.stream(validate).filter(it-> "not_empty".equals(it)).count()>0;
        }
        return false;
    }

    /**
     * 根据 输入数据的 int 值，按下标选择颜色
     * @param value
     * @return
     */
    public String getColor(String value) {
        if (colorMap == null) {
            return null;
        }
        return colorMap.get(value);
    }
}
