package com.example.foreign.exchange.application.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ForeignExchangeApplyRequestVO {
    /**
     * 申请人 ID
     */
    private Long userId;

    /**
     * 交易方向（1-购汇，2-结汇）
     */
    private Integer direction;

    /**
     * 交易币种（ISO 4217，如 "USD"）
     */
    private String currency;

    /**
     * 外币金额
     */
    private BigDecimal amount;

    /**
     * 汇率
     */
    private BigDecimal rate;

    /**
     * 折算本币金额(基于提交时汇率快照)
     */
    private BigDecimal rmbAmount;

    /**
     * 交易主体
     */
    private String transactionSubject;

    /**
     * 主体账号
     */
    private String subjectAccountNo;

    /**
     * 资金用途（≤10字符）
     */
    private String purpose;

    /**
     * 对手方名称
     */
    private String counterparty;

    /**
     * 对手方账号
     */
    private String counterpartyAccountNo;

    /**
     * SWIFT/BIC 码（跨境必填）
     */
    private String swiftBic;

    /**
     * 附件路径 JSON 数组，如 ["/files/a.pdf", "/files/b.jpg"]
     */
    private List<String> attachPaths;
    
    /**
     * 是否为草稿（1-是，0-否）
     */
    private Integer isDraft;
    
    /**
     * 页码，默认1
     */
    private Long page = 1L;
    
    /**
     * 每页条数，默认10
     */
    private Long size = 10L;
}