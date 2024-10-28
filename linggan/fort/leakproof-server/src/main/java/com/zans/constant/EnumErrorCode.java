package com.zans.constant;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public enum EnumErrorCode {

    /**
     *
     */
    SUCCESS(0, "接口调用成功"),
    LOGIN_USER_NULL(1000, "用户名不存在"),
    LOGIN_PASSWORD_ERROR(1001, "密码不正确"),
    LOGIN_USER_LOCKED(1002, "用户账号锁定，请联系管理员"),
    LOGIN_IP_BLOCK(1003, "IP地址锁定，请联系管理员"),
    LOGIN_MAC_BLOCK(1004, "mac地址锁定，请联系管理员"),
    LOGIN_USER_LOGIN_OTHER_PLACE(1005, "其它地点登陆，需要下线"),
    LOGIN_PASSWORD_ERROR_MAX(1006, "错误次数超过最大限制，请1小时后再试"),
    LOGIN_USER_DISABLE(1007, "用户账号停用，请联系管理员"),
    LOGIN_USER_UNOPENED_PC(1008, "用户账号未开启PC登录，请联系管理员"),
    LOGIN_USER_UNOPENED_APP(1009, "用户账号未开启小程序登录，请联系管理员"),
    PERMISSION_NONE(1100, "没有权限，请联系管理员"),
    NEED_LOGIN_AGAIN(1101, "用户session超时，重新登陆"),
    CLIENT_PATH_NOT_FOUND(4000, "接口路径错误"),
    CLIENT_METHOD_NOT_ALLOW(4001, "接口Method类型错误"),
    CLIENT_PARAMS_NULL(4002, "接口参数为空"),
    CLIENT_PARAMS_ERROR(4003, "接口参数错误"),
    CLIENT_PARAMS_CONSTRAINT_ERROR(4005, "接口参数约束错误"),
    CLIENT_PARAMS_BIND_ERROR(4006, "接口参数绑定错误"),
    CLIENT_PARAMS_JSON(4004, "JSON参数序列化错误"),
    CLIENT_RATE_LIMIT(4100, "接口访问过于频繁，请稍后再试"),
    SERVER_UNKNOWN_ERROR(5000, "服务器未知异常"),
    SERVER_IO_ERROR(5001, "服务器IO异常"),
    SERVER_NULL_POINT_ERROR(5002, "服务器空指针异常"),
    SERVER_DB_DUPLICATED_KEY_ERROR(5003, "数据库主键重复异常"),
    SERVER_DB_ERROR(5004, "数据库异常"),
    SERVER_NO_HANDLER_ERROR(5005, "缺少Handler"),
    SERVER_ROLLBACK_ERROR(5006, "数据库事务异常"),
    SERVER_DOWNLOAD_ERROR(5008, "下载文件失败"),
    SERVER_DOWNLOAD_NULL_ERROR(5009, "下载文件失败，数据为空"),
    SERVER_UPLOAD_ERROR(5010, "上传文件失败"),
    SERVER_UPLOAD_MIME_ERROR(5010, "上传文件失败，类型不匹配"),
    SERVER_UPLOAD_MAX_SIZE_ERROR(5010, "上传文件失败，文件过大"),
    SERVER_LOGIC_ERROR(5100, "服务器业务逻辑异常,请联系管理员!"),
    ;

    private int code;

    private String message;

    EnumErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static List<ResponseMessage> getGlobalResponseMessage() {
        Class<?> clz = EnumErrorCode.class;
        List<ResponseMessage> list = new LinkedList<>();
        try {
            Object[] objects = clz.getEnumConstants();
            Method getCode = clz.getMethod("getCode");
            Method getMessage = clz.getMethod("getMessage");
            for (Object obj : objects){
                // 3.调用对应方法，得到枚举常量中字段的值
                Integer code = (Integer)getCode.invoke(obj);
                String message = (String)getMessage.invoke(obj);
                list.add(new ResponseMessageBuilder().code(code).message(message).build());
            }
        } catch (Exception ex) {
            log.error("getGlobalResponseMessage error", ex);
        }
        return list;
    }
}
