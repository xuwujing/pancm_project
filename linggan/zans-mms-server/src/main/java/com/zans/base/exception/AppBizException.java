package com.zans.base.exception;

import java.util.HashMap;
import java.util.Map;

public class AppBizException extends Exception {

    private static final long serialVersionUID = 7366300105025102660L;

    /**
     * 异常代码
     */
    private String code;

    /**
     * 本地化异常信息
     */
    private String localizedMessage;

    /**
     * 异常信息是否不可修改
     */
    private boolean immutable = false;

    /**
     * 是否已输出异常信息
     */
    private boolean logged = false;

    /**
     * 异常数据上下文对象数组
     */
    private Map<String, String> context = new HashMap<String, String>();

    public AppBizException(String code) {
        super("AppBizException[" + code + "].");
        this.code = code;
    }

    public AppBizException(String code, Throwable t) {
        super("AppBizException[" + code + "].", t);
        this.code = code;
    }

    public AppBizException(String code, String message) {
        super("AppBizException[" + code + "]: " + message);
        this.localizedMessage = message;
        this.code = code;
    }

    public AppBizException(String code, String message, Throwable t) {
        super("AppBizException[" + code + "]: " + message, t);
        this.code = code;
        this.localizedMessage = message;
    }

    public AppBizException(String code, String message, Map<String, String> args) {
        super("AppBizException[" + code + "]: " + message);
        this.code = code;
        this.localizedMessage = message;
        context.putAll(args);
    }

    public AppBizException(String code, String message, Map<String, String> args, Throwable t) {
        super("AppBizException[" + code + "]: " + message, t);
        this.code = code;
        this.localizedMessage = message;
        context.putAll(args);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> args) {
        this.context = args;
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
