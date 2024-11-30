package com.tveu.jcode.code_service.core.util;

import com.tveu.jcode.code_service.api.dto.ApiResponse;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(String message, T data, Object metadata) {
        return new ApiResponse<>("success", message, data, metadata);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data, null);
    }
}
