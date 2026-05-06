package com.example.cardApi.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "统一API响应结构")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    /**
     * 错误码，0代表成功，其他代表失败 ，-1
     */
    @Schema(description = "状态码 (0:成功, 其他:失败)", example = "0")
    private int code;
    /**
     * 错误信息，对应错误码
     */
    @Schema(description = "提示信息", example = "成功")
    private String message;
    /**
     * 业务数据
     */
    @Schema(description = "业务数据")
    private T data;
    /**
     * 时间戳
     */
    @Schema(description = "时间戳", example = "1632300000000")
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("成功");
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    // 可以添加其他静态工厂方法，比如错误响应
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
    //高频对象，默认成功
    public static final ApiResponse SUCC = ApiResponse.success("成功");

    //高频对象，失败的默认对象
    public static final ApiResponse FAIL = ApiResponse.error(0, "本次操作无信息"); //本次操作无信息或者系统错误

}