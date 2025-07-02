package com.kingdomsonline.exception;

import com.kingdomsonline.dto.ApiResponse;
import com.kingdomsonline.dto.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("Something went wrong.", 500,
                        List.of(new ErrorDetail("SERVER_ERROR", null, ex.getMessage()))));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("Invalid parameter", 400,
                        List.of(new ErrorDetail("TYPE_MISMATCH", ex.getName(), ex.getMessage()))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorDetail("VALIDATION_ERROR", err.getField(), err.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest()
                .body(ApiResponse.fail("Validation failed", 400, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("Unexpected error", 500,
                        List.of(new ErrorDetail("UNEXPECTED", null, ex.getMessage()))));
    }
}
