package com.zans.portal.enums;

public enum VulLevelEnum {
    //https://www.imooc.com/article/17263
    VUL_LEVEL_INFO("info","提示",0),
    VUL_LEVEL_LOW("low","低危",1),
    VUL_LEVEL_MEDIUM("medium","中危",2),
    VUL_LEVEL_HIGH("high","高危",3),
    VUL_LEVEL_CRITICAL("critical","极危",4);

    private String levelStrCn;

    private String levelStrEn;
    private Integer alertLevel;

    VulLevelEnum(String levelStrEn, String levelStrCn,Integer level) {
        this.levelStrCn = levelStrCn;
        this.levelStrEn = levelStrEn;
        this.alertLevel = level;
    }

    public String getLevelStrCn() {
        return levelStrCn;
    }

    public String getLevelStrEn() {
        return levelStrEn;
    }

    public Integer getAlertLevel() {
        return alertLevel;
    }

    public  static String getVulLevel(String str){
        for (VulLevelEnum levelEnum : VulLevelEnum.values()) {
            if (levelEnum.getLevelStrEn().equalsIgnoreCase(str)){
                return levelEnum.getLevelStrCn();
            }
        }
        return VUL_LEVEL_INFO.getLevelStrCn();
    }
    public  static String getVulLevelStr(Integer level){
        for (VulLevelEnum levelEnum : VulLevelEnum.values()) {
            if (levelEnum.getLevelStrCn().equals(level)){
                return levelEnum.getLevelStrEn();
            }
        }
        return VUL_LEVEL_INFO.getLevelStrEn();
    }

    public static Integer getAlertLevelByCn(String levelStrCn){
        for (VulLevelEnum levelEnum : VulLevelEnum.values()) {
            if (levelEnum.getLevelStrCn().equals(levelStrCn)){
                return levelEnum.getAlertLevel();
            }
        }
        return VUL_LEVEL_INFO.getAlertLevel();
    }
}