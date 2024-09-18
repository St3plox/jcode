package com.tveu.jcode.code_service.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}