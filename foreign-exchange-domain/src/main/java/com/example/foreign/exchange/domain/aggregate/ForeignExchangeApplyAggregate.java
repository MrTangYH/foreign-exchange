package com.example.foreign.exchange.domain.aggregate;

import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;

/**
 * 外汇申请聚合根
 */
public class ForeignExchangeApplyAggregate {
    private ForeignExchangeApply foreignExchangeApply;
    
    public ForeignExchangeApplyAggregate(ForeignExchangeApply foreignExchangeApply) {
        this.foreignExchangeApply = foreignExchangeApply;
    }
    
    public ForeignExchangeApply getForeignExchangeApply() {
        return foreignExchangeApply;
    }
    
    public void setForeignExchangeApply(ForeignExchangeApply foreignExchangeApply) {
        this.foreignExchangeApply = foreignExchangeApply;
    }
}