package com.horseracing.common;

import lombok.Data;

/**
 * 统一响应结果封装
 * @param <T> 泛型参数，返回数据类型
 */
@Data
public class Result<T> {

    /**
     * 响应码：200 成功，500 失败
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求成功，无数据返回
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 请求成功，返回数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 请求成功，自定义消息和数据
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 请求失败
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 请求失败，自定义错误码
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
