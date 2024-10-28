package com.zans.mms.config;



/**
 * @author beixing
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/22
 */

public enum OperationEnum {
    OP_QUERY(0,"查询"),
    OP_SAVE(1,"新增"),
    OP_EDIT(2,"修改"),
    OP_DELETE(3,"删除"),
    OP_BATCH_SAVE(4,"批量新增"),
    OP_BATCH_EDIT(5,"批量修改"),
    OP_BATCH_DELETE(6,"批量删除"),
    OP_IMPORT(11,"导入"),
    OP_EXPORT(12,"导出"),
    OP_VERIFY(21,"审批"),
    ;

    private int code;
    private String value;

    OperationEnum(int code, String value){
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
