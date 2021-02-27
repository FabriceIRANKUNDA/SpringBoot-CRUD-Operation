package com.example.crudapi.exceptionHandling;

import org.springframework.http.HttpStatus;

public class StudentException extends RuntimeException{
    private String errorMessage;
    private String errorCode;
    private HttpStatus httpStatus;

    public StudentException(String errorMessage, String errorCode, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

