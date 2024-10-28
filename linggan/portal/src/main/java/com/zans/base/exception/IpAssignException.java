package com.zans.base.exception;

/**
 * IP分配异常
 * @author xv
 * @since 2020/3/23 14:26
 */
public class IpAssignException extends RuntimeException {

    public IpAssignException(String message) {
        super(message);
    }
}
