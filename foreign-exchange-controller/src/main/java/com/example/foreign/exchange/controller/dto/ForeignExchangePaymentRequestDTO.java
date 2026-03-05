package com.example.foreign.exchange.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇付款请求DTO
 */
@Data
public class ForeignExchangePaymentRequestDTO {
    private Long id;
    private String orderNo;
    private String applyNo;
    private Integer direction;
    private String currency;
    private BigDecimal amount;
    private BigDecimal rmbAmount;
    private BigDecimal rate;
    private String transactionSubject;
    private String subjectAccountNo;
    private String counterparty;
    private String counterpartyAccountNo;
    private String swiftBic;
    private Integer status;
    private Long userId;
    private LocalDateTime createTime;
}