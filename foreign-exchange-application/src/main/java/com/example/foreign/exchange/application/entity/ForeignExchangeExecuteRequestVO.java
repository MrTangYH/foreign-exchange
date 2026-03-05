package com.example.foreign.exchange.application.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 外汇执行单请求VO
 */
@Data
public class ForeignExchangeExecuteRequestVO {
    /**
     * 关联申请单号（非唯一）
     */
    private String applyNo;
    
    /**
     * 交易方向（1-购汇，2-结汇）
     */
    private Integer direction;
    
    /**
     * 交易币种
     */
    private String currency;
    
    /**
     * 外币金额
     */
    private BigDecimal amount;
    
    /**
     * 本币金额（来自申请快照）
     */
    private BigDecimal rmbAmount;
    
    /**
     * 汇率
     */
    private BigDecimal rate;
    
    /**
     * 交易主体名称
     */
    private String transactionSubject;
    
    /**
     * 交易主体账号
     */
    private String subjectAccountNo;
    
    /**
     * 对手方名称
     */
    private String counterparty;
    
    /**
     * 对手方银行账号
     */
    private String counterpartyAccountNo;
    
    /**
     * SWIFT/BIC
     */
    private String swiftBic;
    
    /**
     * 状态：1-执行单已生成，2-付款单已生成，3-已完成，4-已取消
     */
    private Integer status;
    
    /**
     * 创建人ID（财务专员）
     */
    private Long userId;
}