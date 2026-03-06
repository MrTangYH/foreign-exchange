package com.example.foreign.exchange.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇账户实体
 */
@Data
@TableName("foreign_exchange_account")
public class ForeignExchangeAccount {
    /**
     * 主键 ID（自增，起始为1）
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 账户编号（如 MAIN_CNY_001）
     */
    private String accountNo;
    
    /**
     * 总余额
     */
    private BigDecimal balance;
    
    /**
     * 币种
     */
    private String currency;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}