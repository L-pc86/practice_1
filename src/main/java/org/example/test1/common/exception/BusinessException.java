package org.example.test1.common.exception;

import org.example.test1.common.ResultCodeEnum;

public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMsg());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
