package com.zans.portal.enums;

public enum JobStatusEnum {
    STATUS_NEW(0, "初始化"),
    STATUS_ERROR(1, "错误"),
    STATUS_PENDING(2, "排队"),
    STATUS_RUNNING(3, "执行中"),
    STATUS_TIMEOUT(4, "超时"),
    STATUS_STOPPED(5, "人工终止"),
    STATUS_COMPLETED(6, "正常结束");


    private Integer code;
    private String name;

    JobStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Integer getCodeByEnName(String englishName) {
        for (JobStatusEnum jobStatusEnum : JobStatusEnum.values()) {
            if (jobStatusEnum.getName().equals(englishName)) {
                return jobStatusEnum.getCode();
            }
        }
        return null;
    }
}