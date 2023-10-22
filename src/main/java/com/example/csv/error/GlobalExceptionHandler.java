package com.example.csv.error;

import com.example.csv.exception.CustomException;
import com.example.csv.exception.FutureDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FutureDateException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(FutureDateException ex) {
        return buildErrorResponse(ex, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalEx(IllegalArgumentException ex) {
        return buildErrorResponse(ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = ErrorResponse.builder().status(status.value()).message(ex.getMessage()).errorCode(ex instanceof CustomException ? ((CustomException) ex).getErrorCode() : "DEFAULT_ERROR_CODE").build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
