package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.dto.ApiResponse;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.util.ResponseUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<String>> handle(ServiceException exception) {

        var response = ResponseUtil.error("service error", exception.getMessage());
        var status = exception.getErrorCode().getHttpStatus().value();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handle(Exception exception) {

        var response = ResponseUtil.error("data integrity error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(RuntimeException exception) {

        var response = ResponseUtil.error("unhandled error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}