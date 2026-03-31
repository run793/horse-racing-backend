package com.horseracing.handler;

import com.horseracing.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 * 统一捕获并处理所有 Controller 抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常 - @RequestBody 方式
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "参数校验失败";
        log.error("参数校验失败: {}", message);
        return Result.error(400, message);
    }

    /**
     * 处理参数校验异常 - 普通参数方式
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            message.append(violation.getMessage()).append(",");
        }
        String resultMessage = message.length() > 0
                ? message.substring(0, message.length() - 1)
                : "参数校验失败";
        log.error("参数校验失败: {}", resultMessage);
        return Result.error(400, resultMessage);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getFieldError() != null
                ? e.getFieldError().getDefaultMessage()
                : "参数绑定失败";
        log.error("参数绑定失败: {}", message);
        return Result.error(400, message);
    }

    /**
     * 处理业务异常（可根据需要自定义业务异常类）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "服务器内部错误，请联系管理员");
    }
}
