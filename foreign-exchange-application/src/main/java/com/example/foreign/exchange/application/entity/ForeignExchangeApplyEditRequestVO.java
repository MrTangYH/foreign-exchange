package com.example.foreign.exchange.application.entity;

import lombok.Data;

@Data
public class ForeignExchangeApplyEditRequestVO extends ForeignExchangeApplyRequestVO {
    /**
     * 申请编号（唯一键，用于查询）
     */
    private String applyNo;
}
