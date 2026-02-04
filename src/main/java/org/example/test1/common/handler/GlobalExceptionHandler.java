package org.example.test1.common.handler;

import org.example.test1.common.Result;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.common.exception.BusinessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ 业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMsg());
    }

    // 2️⃣ 参数校验异常（后面会用到）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        return Result.error(ResultCodeEnum.PARAM_ERROR);
    }

    // 3️⃣ 兜底异常（非常重要）
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace(); // 方便排查
        return Result.error(ResultCodeEnum.SYSTEM_ERROR);
    }
}
