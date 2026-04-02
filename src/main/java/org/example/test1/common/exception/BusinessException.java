package org.example.test1.common.exception;

import lombok.Getter;
import org.example.test1.common.ResultCodeEnum;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    public BusinessException(ResultCodeEnum codeEnum, String message) {
        super(message);
        this.code = codeEnum.getCode();
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
