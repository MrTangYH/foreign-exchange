package com.example.foreign.exchange.application.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇付款请求VO
 */
@Data
public class ForeignExchangePaymentRequestVO {
    /**
     * 关联外汇订单编号（一对一）
     */
    private String orderNo;
    
    /**
     * 付款金额（CNY，等于order.rmb_amount）
     */
    private BigDecimal paymentAmount;
    
    /**
     * 付款币种
     */
    private String payCurrency;
    
    /**
     * 收款金额
     */
    private BigDecimal receivedAmount;
    
    /**
     * 收款币种
     */
    private String receivedCurrency;
    
    /**
     * 交易主体账号
     */
    private String subjectAccountNo;
    
    /**
     * 收款方银行账号
     */
    private String counterpartyAccountNo;
    
    /**
     * SWIFT/BIC
     */
    private String swiftBic;
    
    /**
     * 状态：1-待付款，2-已付款，3-付款失败，4-已作废
     */
    private Integer status;
    
    /**
     * 实际付款时间
     */
    private LocalDateTime paymentTime;
    
    /**
     * 付款失败原因（如“余额不足”）
     */
    private String failureReason;
    
    /**
     * 操作人ID（发起付款者）
     */
    private Long userId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}