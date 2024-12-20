package com.pancm.test.xdecoder.vo;


/**
 * 统一返回
 */


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

public class Result<T> implements Serializable {
    public static final int RESULT_OK;
    private static final int RESULT_ERROR;
    private static final String COMMON_ERROR_MESSAGE = "发生错误!";
    private static final long serialVersionUID = 8408096798628085377L;

    private int code;
    private String message;
    private String detail;
    private long timestamp;
    private T data;
    private Map<String,Object> meta;

    private Result() {
    }

    public static Result<Boolean> ok() {
        return ok(true);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> rj = new Result<>();
        rj.code = RESULT_OK;
        rj.message = "SUCCESS";
        rj.timestamp = System.currentTimeMillis();
        rj.data = data;
        return rj;
    }

    public static <T> Result<T> ok(T data, Map<String,Object> meta) {
        Result<T> rj = new Result<>();
        rj.code = RESULT_OK;
        rj.message = "SUCCESS";
        rj.timestamp = System.currentTimeMillis();
        rj.data = data;
        rj.meta = meta;
        return rj;
    }

    public static <T> Result<T> badRequest(int code) {
        Result<T> rj = new Result<>();
        rj.code = code;
        rj.message = COMMON_ERROR_MESSAGE;
        rj.timestamp = System.currentTimeMillis();
        return rj;
    }

    public static <T> Result<T> badRequest(String message) {
        Result<T> rj = new Result<>();
        rj.code = RESULT_ERROR;
        rj.message = StringUtils.defaultIfEmpty(message, COMMON_ERROR_MESSAGE);
        rj.timestamp = System.currentTimeMillis();
        return rj;
    }

    public static <T> Result<T> badRequest(String message, String detail) {
        Result<T> rj = new Result<>();
        rj.code = RESULT_ERROR;
        rj.message = StringUtils.defaultIfEmpty(message, COMMON_ERROR_MESSAGE);
        rj.detail = StringUtils.defaultIfEmpty(detail, COMMON_ERROR_MESSAGE);
        rj.timestamp = System.currentTimeMillis();
        return rj;
    }

    public static <T> Result<T> badRequest(int code, String message) {
        Result<T> rj = new Result<>();
        rj.code = code;
        rj.message = StringUtils.defaultIfEmpty(message, COMMON_ERROR_MESSAGE);
        rj.timestamp = System.currentTimeMillis();
        return rj;
    }



    public static <T> Result<T> badRequest(int code, String message, String detail) {
        Result<T> rj = badRequest(message, detail);
        rj.code = code;
        return rj;
    }

    public static <T> Result<T> badRequest(String message, T data) {
        return badRequest(RESULT_ERROR,message,data);
    }


    public static <T> Result<T> badRequest(int code,String message, T data) {
        Result<T> rj = new Result<>();
        rj.code = code;
        rj.message = StringUtils.defaultIfEmpty(message, COMMON_ERROR_MESSAGE);
        rj.timestamp = System.currentTimeMillis();
        rj.data = data;
        return rj;
    }

    public boolean isSuccess() {
        return this.code == RESULT_OK;
    }



    public String errorMsg() {
        return this.message + this.detail;
    }


    public String getMessage() {
        return this.message;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public String getDetail() {
        return this.detail;
    }

    public Result<T> detail(String detail) {
        this.detail = detail;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCode() {
        return this.code;
    }

    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Map<String,Object> getMeta() {
        return this.meta;
    }

    public Result<T> meta(Map<String,Object> meta) {
        this.meta = meta;
        return this;
    }



    static {
        RESULT_OK = 0;
        RESULT_ERROR = 400;
    }
}
