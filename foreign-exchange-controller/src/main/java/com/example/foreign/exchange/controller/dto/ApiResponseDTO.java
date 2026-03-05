package com.example.foreign.exchange.controller.dto;

import lombok.Data;

import java.util.Map;

/**
 * API响应DTO
 */
@Data
public class ApiResponseDTO {
    /**
     * 状态码
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private Object data;
    
    /**
     * 成功响应
     * @param message 消息
     * @param data 数据
     * @return 响应对象
     */
    public static ApiResponseDTO success(String message, Object data) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    /**
     * 失败响应
     * @param code 状态码
     * @param message 消息
     * @return 响应对象
     */
    public static ApiResponseDTO fail(int code, String message) {
        ApiResponseDTO response = new ApiResponseDTO();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}