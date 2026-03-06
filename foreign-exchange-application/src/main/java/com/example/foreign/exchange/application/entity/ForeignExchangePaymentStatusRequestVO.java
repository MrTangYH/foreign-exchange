package com.example.foreign.exchange.application.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 外汇付款状态变更请求VO
 */
@Data
public class ForeignExchangePaymentStatusRequestVO {
    /**
     * 付款单号
     */
    private String paymentNo;
    
    /**
     * 交易主体账号
     */
    private String subjectAccountNo;
    
    /**
     * 付款币种
     */
    private String payCurrency;
    
    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;
    
    /**
     * 状态
     */
    private Integer status;
}