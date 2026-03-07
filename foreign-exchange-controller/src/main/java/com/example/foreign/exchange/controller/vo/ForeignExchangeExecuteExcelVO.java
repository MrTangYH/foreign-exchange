package com.example.foreign.exchange.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇执行Excel导出VO
 */
@Data
public class ForeignExchangeExecuteExcelVO {
    /**
     * 执行单编号
     */
    @ExcelProperty("执行单编号")
    private String executeNo;
    
    /**
     * 申请单编号
     */
    @ExcelProperty("申请单编号")
    private String applyNo;
    
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
     * 执行金额
     */
    @ExcelProperty("执行金额")
    private BigDecimal executeAmount;
    
    /**
     * 执行汇率
     */
    @ExcelProperty("执行汇率")
    private BigDecimal executeRate;
    
    /**
     * 执行时间
     */
    @ExcelProperty("执行时间")
    private LocalDateTime executeTime;
    
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