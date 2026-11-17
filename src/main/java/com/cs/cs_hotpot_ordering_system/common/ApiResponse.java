/**
 * 文件说明：统一接口返回结果类。
 * 作用：让前端得到统一格式的 JSON，包括是否成功、提示信息和实际数据。
 */
package com.cs.cs_hotpot_ordering_system.common;

/** @param <T> 接口实际返回的数据类型 */
public record ApiResponse<T>(boolean success, String message, T data) {
    /** 创建一个默认成功的返回结果。 */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data);
    }

    /** 创建一个带自定义提示语的成功结果。 */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /** 创建一个失败结果，失败时没有业务数据。 */
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
