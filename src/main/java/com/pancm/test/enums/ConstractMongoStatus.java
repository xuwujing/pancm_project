package com.pancm.test.enums;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */

public enum ConstractMongoStatus {

    NEW(3,"新增"),

    UPDATE(2,"修改"),

    DELETE(1,"删除"),

    GENERATOR(0,"普通无状态"),

    // 是否提交审核
    REVIEW(5,"提交审核"),

    NO_REVIEW(-5,"不提交审核"),

    //变更流程相关

    ONLY_CONTRACT_MATERIAL(6,"仅涉及合同材料修改"),

    NOT_ONLY_CONTRACT_MATERIAL(7,"需要合同档案部审核"),

    CRM_ACC_EXPEN_CHANGE(8,"涉及实收款变更"),

    NO_CRM_ACC_EXPEN_CHANGE(9,"不涉及实收款变更"),

    //历史版本列表

    UPDATE_PRODUCT(10,"变更商品"),

    UPDATE_ACC_EXPEN(11,"变更实收款"),

    UPDATE_RECEIVAL(20,"变更收款计划"),

    UPDATE_ATTACHMENT(12,"变更附件"),

    UPDATE_CONTRACT(13,"变更合同基本信息"),

    UPDATE_APPROVAL(14,"变更审批单"),

    //变更历史状态描述

    MODIFY_NOMORE(19,"初始状态"),

    MODIFY_UPDATE(15,"已更新"),

    MODIFY_REVIEW_ING(16,"审核中"),

    MODIFY_REVIEW_PASS(17,"审核通过"),

    MODIFY_REVIEW_NO_PASS(18,"审核未通过")

    ;

    private final Integer status;

    private final String desc;

    private ConstractMongoStatus(final Integer status,final String desc){
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static Integer getValue(Integer value) {
        ConstractMongoStatus[] businessModeEnums = values();
        for (ConstractMongoStatus businessModeEnum : businessModeEnums) {
            if (businessModeEnum.status().equals(value)) {
                return businessModeEnum.status();
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        ConstractMongoStatus[] businessModeEnums = values();
        for (ConstractMongoStatus businessModeEnum : businessModeEnums) {
            if (businessModeEnum.status().equals(value)) {
                return businessModeEnum.desc();
            }
        }
        return null;
    }

    public Integer status(){
        return this.status;
    }

    public String desc(){
        return this.desc;
    }


}
