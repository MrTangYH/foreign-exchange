package com.example.foreign.exchange.application.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇付款查询请求VO
 */
@Data
public class ForeignExchangePaymentQueryRequestVO {
    /**
     * 付款单号
     */
    private String paymentNo;
    
    /**
     * 付款金额下限
     */
    private BigDecimal paymentAmountMin;
    
    /**
     * 付款金额上限
     */
    private BigDecimal paymentAmountMax;
    
    /**
     * 收款金额下限
     */
    private BigDecimal receivedAmountMin;
    
    /**
     * 收款金额上限
     */
    private BigDecimal receivedAmountMax;
    
    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;
    
    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
    
    /**
     * 页码，默认1
     */
    private Long page = 1L;
    
    /**
     * 每页条数，默认10
     */
    private Long size = 10L;
}