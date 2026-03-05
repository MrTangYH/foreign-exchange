package com.example.foreign.exchange.domain.enums;

import lombok.Getter;

/**
 * 执行单状态枚举
 */
@Getter
public enum ExecuteStatusEnum {

    GENERATED(1, "执行单已生成"),
    PAYMENT_GENERATED(2, "执行单已生成付款单"),
    CANCELLED(3, "执行单已取消"),
    VOIDED(4, "执行单已作废");

    private final Integer code;
    private final String message;

    ExecuteStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     */
    public static ExecuteStatusEnum getByCode(Integer code) {
        for (ExecuteStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
