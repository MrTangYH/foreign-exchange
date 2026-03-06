package com.example.foreign.exchange.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇账户交易记录实体
 */
@Data
@TableName("foreign_exchange_account_transaction")
public class ForeignExchangeAccountTransaction {
    /**
     * 主键 ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 账户 ID
     */
    private String accountNo;
    
    /**
     * 关联付款单ID（可选）
     */
    private String paymentNo;
    
    /**
     * 类型：DEDUCT（扣款）
     */
    private String type;
    
    /**
     * 变动金额（正数表示支出）
     */
    private BigDecimal amount;
    
    /**
     * 操作后账户余额
     */
    private BigDecimal balanceAfter;
    
    /**
     * 交易时间
     */
    private LocalDateTime transactionTime;
}