package com.example.foreign.exchange.controller.dto;

import lombok.Data;

/**
 * 外汇执行单查询请求DTO
 */
@Data
public class ForeignExchangeExecuteQueryRequestDTO extends ForeignExchangeExecuteRequestDTO {
    /**
     * 执行单号
     */
    private String orderNo;
    
    /**
     * 页码，默认1
     */
    private Long page = 1L;
    
    /**
     * 每页条数，默认10
     */
    private Long size = 10L;
}