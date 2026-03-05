package com.example.foreign.exchange.application.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 外汇申请响应VO
 */
@Data
public class ForeignExchangeApplyResponseVO {
    /**
     * 主键 ID（自增）
     */
    private Long id;
    
    /**
     * 申请编号（FXAPPLY+YYYYMMDD+6位序列）
     */
    private String applyNo;
    
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
     * 状态码
     */
    private Integer status;
    
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 是否为草稿（1-是，0-否）
     */
    private Integer isDraft;
}