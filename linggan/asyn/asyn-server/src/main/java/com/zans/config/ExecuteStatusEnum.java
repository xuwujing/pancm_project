package com.zans.config;

/**
 * @author beixing
 * @Title: asyn-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/30
 */

public enum ExecuteStatusEnum {

    INIT(0, "未开始"),
    RUNNING(1, "进行中"),
    RECEIVE(2, "已接收"),
    FAIL(3, "失败"),
    RETURN_SUCCESS(4, "返回成功"),
    RETURN_FAIL(5, "返回失败"),
    ;


    private int status;
    private String name;

    ExecuteStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
