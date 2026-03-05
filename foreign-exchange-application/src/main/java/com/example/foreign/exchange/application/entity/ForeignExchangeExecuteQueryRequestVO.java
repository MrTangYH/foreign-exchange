package com.example.foreign.exchange.application.entity;

import lombok.Data;

/**
 * 外汇执行单查询请求VO
 */
@Data
public class ForeignExchangeExecuteQueryRequestVO extends ForeignExchangeExecuteRequestVO {
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