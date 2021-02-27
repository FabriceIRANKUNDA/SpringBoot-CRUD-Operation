package com.example.crudapi.exceptionHandling;

import org.springframework.http.HttpStatus;

public class AppError {
    public String errorCode;
    public String message;
    public HttpStatus httpStatus;

    public AppError(String message, String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
