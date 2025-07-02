package com.kingdomsonline.dto;

import lombok.Getter;

@Getter
public class ErrorDetail {
    private String code;
    private String field;
    private String message;

    public ErrorDetail() {}

    public ErrorDetail(String code, String field, String message) {
        this.code = code;
        this.field = field;
        this.message = message;
    }

    // Getters and Setters
}
