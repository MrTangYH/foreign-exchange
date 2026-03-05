package com.example.foreign.exchange.controller.dto;

import lombok.Data;

/**
 * 外汇申请草稿编辑DTO
 */
@Data
public class ForeignExchangeApplyEditRequestDTO extends ForeignExchangeApplyRequestDTO {
    /**
     * 申请编号（唯一键，用于查询）
     */
    private String applyNo;
}