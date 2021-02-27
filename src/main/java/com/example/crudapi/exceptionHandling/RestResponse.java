package com.example.crudapi.exceptionHandling;

import org.springframework.http.HttpStatus;

public class RestResponse<T> {

    private T data;
    private AppError error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AppError getError() {
        return error;
    }

    public void setError(AppError error) {
        this.error = error;
    }


}
