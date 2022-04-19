package com.stone.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -3776238194059561696L;

    private T data;

    private Boolean status;

    private String code;

    private String errorMsg;

    private Date requestTime = new Date();

    public void setData(T data) {
        this.status = Boolean.TRUE;
        this.data = data;
        this.code = ResultCode.CODE_SUCCESS;
    }

    public void setErrorMsg(String errorMsg) {
        this.status = Boolean.FALSE;
        this.errorMsg = errorMsg;
        this.code = ResultCode.CODE_GLOBAL_EXCEPTION;
    }
}
