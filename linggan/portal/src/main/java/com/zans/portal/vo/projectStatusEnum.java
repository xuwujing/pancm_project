package com.zans.portal.vo;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/13
 */
public enum projectStatusEnum {


    IN_CONSTRUCTION(0,"建设中"),
    QUALITY_GUARANTEE(1,"质保"),
    OVER_GUARANTEE(2,"过保"),
    MAINTENANCE(3,"维保");

    private int code;

    private String desc;

    projectStatusEnum(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
