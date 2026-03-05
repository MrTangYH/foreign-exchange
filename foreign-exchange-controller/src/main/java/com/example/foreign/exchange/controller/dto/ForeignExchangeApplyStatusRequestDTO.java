package com.example.foreign.exchange.controller.dto;

import lombok.Data;

@Data
public class ForeignExchangeApplyStatusRequestDTO {
    /**
     * 申请编号
     */
    private String applyNo;

    /**
     * 状态码
     */
    private Integer status;
}
