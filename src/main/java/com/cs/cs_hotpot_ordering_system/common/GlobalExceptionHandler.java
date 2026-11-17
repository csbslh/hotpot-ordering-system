/**
 * 文件说明：全局异常处理类。
 * 作用：统一捕获业务参数错误和未预期异常，避免 Controller 重复编写 try-catch。
 */
package com.cs.cs_hotpot_ordering_system.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 将业务参数异常转换为 HTTP 400 返回。 */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(IllegalArgumentException exception) {
        return ApiResponse.fail(exception.getMessage());
    }

    /** 将未处理异常转换为 HTTP 500 返回，避免把内部错误细节暴露给用户。 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception exception) {
        return ApiResponse.fail("服务器处理失败，请稍后重试");
    }
}
