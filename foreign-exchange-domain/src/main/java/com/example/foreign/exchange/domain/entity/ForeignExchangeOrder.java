package com.example.foreign.exchange.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇执行单实体
 */
@Data
public class ForeignExchangeOrder {
    /**
     * 主键 ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 执行单号（FXORDER+YYYYMMDD+8位序列）
     */
    private String orderNo;
    
    /**
     * 关联申请单号（非唯一）
     */
    private String applyNo;
    
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
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
}