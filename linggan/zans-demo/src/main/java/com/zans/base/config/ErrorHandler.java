package com.zans.base.config;

import com.alibaba.fastjson.JSONException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zans.base.exception.BusinessException;
import com.zans.base.exception.RollbackException;
import com.zans.base.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;

import static com.zans.base.config.BaseConstants.SEPARATOR_SPACE;
import static com.zans.base.config.EnumErrorCode.*;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        return "";
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(SERVER_DB_DUPLICATED_KEY_ERROR).appendMessage(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ApiResult handleDataAccessException(DataAccessException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(SERVER_DB_ERROR).appendMessage("接口参数错误");
    }

    @ExceptionHandler(RollbackException.class)
    public ApiResult handleRollbackException(RollbackException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(SERVER_ROLLBACK_ERROR).appendMessage(e.getMessage());
    }


    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ApiResult noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(SERVER_NO_HANDLER_ERROR).appendMessage(e.getRequestURL()).appendMessage(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ApiResult nullPointerException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return ApiResult.error(SERVER_NULL_POINT_ERROR).appendMessage(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e) {
        log.error("业务逻辑异常", e);
        return ApiResult.error(SERVER_LOGIC_ERROR);
    }

    /**
     * 错误 method
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult methodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        return ApiResult.error(CLIENT_METHOD_NOT_ALLOW).appendMessage(ex.getMethod());
    }

    /**
     * jwt token 过期异常
     * @param ex
     * @return
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ApiResult tokenExpiredException(TokenExpiredException ex) {
        return ApiResult.error(NEED_LOGIN_AGAIN).appendMessage(ex.getMessage());
    }


    /**
     * 单个参数违反约束
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult validationExceptionHandler(ConstraintViolationException e) {

        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(v -> sb.append(v == null ? "" : v.getPropertyPath() + ": " + v.getMessage())
                                                   .append(SEPARATOR_SPACE).append(" "));

        String errorMsg = sb.toString();
        log.warn("ConstraintViolationException#{}", errorMsg);

        return ApiResult.error(CLIENT_PARAMS_CONSTRAINT_ERROR).appendMessage(errorMsg);

    }

    @ExceptionHandler(BindException.class)
    public ApiResult bindExceptionHandler(BindException e) {

        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(v -> sb.append(v.getDefaultMessage()).append(SEPARATOR_SPACE));

        String errorMsg = sb.toString();
        log.warn("BindException#{}", errorMsg);

        return ApiResult.error(CLIENT_PARAMS_BIND_ERROR).appendMessage(errorMsg);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult validationExceptionHandler(MethodArgumentNotValidException e) {

        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(v -> sb.append(v.getDefaultMessage()).append(SEPARATOR_SPACE));

        String errorMsg = sb.toString();
        log.warn("MethodArgumentNotValidException#{}", errorMsg);

        return ApiResult.error(CLIENT_PARAMS_ERROR).appendMessage(errorMsg);

    }

    @ExceptionHandler(JSONException.class)
    public ApiResult jsonExceptionHandler(JSONException e) {
        String errorMsg = e.getMessage();
        log.warn("JSONException#{}", errorMsg);
        return ApiResult.error(CLIENT_PARAMS_JSON).appendMessage(errorMsg);

    }

    @ExceptionHandler(BusinessException.class)
    public ApiResult businessExceptionHandler(BusinessException e) {
        String errorMsg = e.getMessage();
        log.error("BusinessException#{}", errorMsg);
        return ApiResult.error(errorMsg);

    }

//    @ExceptionHandler(AppBizException.class)
//    public ApiResult JSONExceptionHandler(AppBizException e) {
//        String errorMsg = e.getLocalizedMessage();
//        log.warn("AppBizException#{}", errorMsg);
//
//        return new ApiResult(ApiResultStatus.SYSTEM_ERROR, errorMsg);
//
//    }
//
//    @ExceptionHandler(AppRtException.class)
//    public ApiResult AppRtExceptionHandler(AppRtException e) {
//        String errorMsg = e.getLocalizedMessage();
//        log.warn("AppRtException#{}", errorMsg);
//
//        return new ApiResult(ApiResultStatus.SYSTEM_ERROR, errorMsg);
//
//    }
//
//    @ExceptionHandler(InterruptedException.class)
//    public ApiResult InterruptedExceptionHandler(InterruptedException e) {
//        String errorMsg = e.getLocalizedMessage();
//        log.warn("AppRtException#{}", errorMsg);
//
//        return new ApiResult(ApiResultStatus.SYSTEM_ERROR, errorMsg);
//
//    }
}
