package com.tveu.jcode.code_service.core.exception;


import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public ServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

}