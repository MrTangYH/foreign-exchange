package com.example.foreign.exchange.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 外汇申请Excel导出VO
 */
@Data
public class ForeignExchangeApplyExcelVO {
    /**
     * 申请编号
     */
    @ExcelProperty("申请编号")
    private String applyNo;
    
    /**
     * 申请人 ID
     */
    @ExcelProperty("申请人ID")
    private Long userId;
    
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
     * 外币金额
     */
    @ExcelProperty("外币金额")
    private BigDecimal amount;
    
    /**
     * 汇率
     */
    @ExcelProperty("汇率")
    private BigDecimal rate;
    
    /**
     * 本币金额
     */
    @ExcelProperty("本币金额")
    private BigDecimal rmbAmount;
    
    /**
     * 交易主体
     */
    @ExcelProperty("交易主体")
    private String transactionSubject;
    
    /**
     * 主体账号
     */
    @ExcelProperty("主体账号")
    private String subjectAccountNo;
    
    /**
     * 资金用途
     */
    @ExcelProperty("资金用途")
    private String purpose;
    
    /**
     * 对手方名称
     */
    @ExcelProperty("对手方名称")
    private String counterparty;
    
    /**
     * 对手方账号
     */
    @ExcelProperty("对手方账号")
    private String counterpartyAccountNo;
    
    /**
     * SWIFT/BIC码
     */
    @ExcelProperty("SWIFT/BIC码")
    private String swiftBic;
    
    /**
     * 状态
     */
    @ExcelProperty("状态")
    private String status;
    
    /**
     * 提交时间
     */
    @ExcelProperty("提交时间")
    private LocalDateTime submitTime;
    
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}