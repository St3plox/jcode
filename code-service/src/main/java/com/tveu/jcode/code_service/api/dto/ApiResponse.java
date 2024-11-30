package com.tveu.jcode.code_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private Object metadata;
}
