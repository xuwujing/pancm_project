package com.zans.vo;


import lombok.Data;

import java.io.Serializable;


import static com.zans.commons.contants.Constants.CODE_ERROR_UNKNOWN;
import static com.zans.commons.contants.Constants.CODE_SUCCESS;

/**
 * @Author pancm
 * @Description 接口返回类
 * @Date
 * @Param
 * @return
 **/
@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 10000000L;

    private int code;

    private String message;

    private T data;

    private Object stackTrace;

    public ApiResult message(String input) {
        this.setMessage(input);
        return this;
    }

    public ApiResult appendMessage(String input) {
        this.message += " " + input;
        return this;
    }

    public ApiResult data(T input) {
        this.setData(input);
        return this;
    }

    public static ApiResult success() {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(CODE_SUCCESS);
        resultMsg.setMessage("");
        return resultMsg;
    }

    public static ApiResult success(Object data) {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(CODE_SUCCESS);
        resultMsg.setData(data);
        resultMsg.setMessage("");
        return resultMsg;
    }

    public static ApiResult error(String message) {
        return error(CODE_ERROR_UNKNOWN, message);
    }

    public static ApiResult error(int code, String message) {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(code);
        resultMsg.setMessage(message);
        return resultMsg;
    }



    public static ApiResult error(int code, String message, Object... data) {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(code);
        resultMsg.setMessage(message);
        if (data != null) {
            if (data.length >= 1) {
                resultMsg.setData(data[0]);
            }
            if (data.length >= 2) {
                resultMsg.setStackTrace(data[1]);
            }
        }
        return resultMsg;
    }
}
