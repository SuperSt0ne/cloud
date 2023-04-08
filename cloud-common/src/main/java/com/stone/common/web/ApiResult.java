package com.stone.common.web;

import lombok.Data;

@Data
public class ApiResult<T> {

    private Long requestId;

    private T data;

    private Boolean success;

    private Integer errorCode;

    private String message;

    private String stackTrance;


    public void setMessage(String message) {
        this.message = message;
        this.success = false;
    }

    public void setData(T data) {
        this.data = data;
        this.success = true;
    }
}
