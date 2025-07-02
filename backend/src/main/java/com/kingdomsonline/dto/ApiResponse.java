package com.kingdomsonline.dto;

import lombok.Getter;

import java.util.List;
@Getter
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private int status;
    private T data;
    private List<ErrorDetail> errors;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, int status, T data, List<ErrorDetail> errors) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, 200, data, null);
    }

    public static <T> ApiResponse<T> fail(String message, int status, List<ErrorDetail> errors) {
        return new ApiResponse<>(false, message, status, null, errors);
    }

    public static <T> ApiResponse<T> fail(String message, int status) {
        return new ApiResponse<>(false, message, status, null, null);
    }

    // Getters and Setters
}
