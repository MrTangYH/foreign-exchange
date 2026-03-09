package com.example.foreign.exchange.application.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇付款Excel导出VO
 */
@Data
public class ForeignExchangePaymentExcelVO {
    /**
     * 付款单编号
     */
    @ExcelProperty("付款单编号")
    private String paymentNo;
    
    /**
     * 执行单编号
     */
    @ExcelProperty("执行单编号")
    private String executeNo;
    
    /**
     * 交易方向
     */
    @ExcelProperty("交易方向")
    private String direction;
    
    /**
     * 交易币种
     */
    @ExcelProperty("交易币种")
    private String currency;
    
    /**
     * 付款金额
     */
    @ExcelProperty("付款金额")
    private BigDecimal paymentAmount;
    
    /**
     * 付款账户
     */
    @ExcelProperty("付款账户")
    private String subjectAccountNo;
    
    /**
     * 收款账户
     */
    @ExcelProperty("收款账户")
    private String counterpartyAccountNo;
    
    /**
     * 付款时间
     */
    @ExcelProperty("付款时间")
    private LocalDateTime paymentTime;
    
    /**
     * 状态
     */
    @ExcelProperty("状态")
    private String status;
    
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}