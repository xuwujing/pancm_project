package com.zans.utils;


import com.zans.constant.EnumErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import static com.zans.constant.BaseConstants.CODE_TOKEN_EX;


@Data
@ApiModel(value = "返回值", description="通用返回值")
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 10000000L;

    @ApiModelProperty(value = "返回状态码；0:成功；其它，失败")
    private int code;

    @ApiModelProperty(value = "补充信息，显示给前端用户")
    private String message;

    @ApiModelProperty(value = "接口请求结果")
    private T data;

    @ApiModelProperty(value = "异常的堆栈信息")
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
        resultMsg.setCode(0);
        resultMsg.setMessage("");
        return resultMsg;
    }

    public static ApiResult success(Object data) {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(0);
        resultMsg.setData(data);
        resultMsg.setMessage("");
        return resultMsg;
    }

    public static ApiResult error(String message) {
        return error(500, message);
    }

    public static ApiResult conflict(String message) {
        return error(501, message);
    }

    public static ApiResult expired(String message) {
        return error(CODE_TOKEN_EX, message);
    }

    public static ApiResult error(int code, String message) {
        ApiResult resultMsg = new ApiResult();
        resultMsg.setCode(code);
        resultMsg.setMessage(message);
        return resultMsg;
    }

    public static ApiResult error(EnumErrorCode code, Object... data) {
        return error(code.getCode(), code.getMessage(), data);
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
