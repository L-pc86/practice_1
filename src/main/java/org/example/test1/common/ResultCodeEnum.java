package org.example.test1.common;

public enum ResultCodeEnum {

    SUCCESS(200, "success"),
    FAIL(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),
    SYSTEM_ERROR(500, "系统错误");  // 添加这行

    private final Integer code;
    private final String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

