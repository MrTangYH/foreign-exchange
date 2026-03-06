package com.example.foreign.exchange.domain.enums;

import lombok.Getter;

/**
 * 支付状态结果枚举
 */
@Getter
public enum PaymentStatusResultEnum {

    /**
     * 待付款
     */
     PENDING_PAYMENT(0, "待付款"),

    /**
     * 付款成功
     */
    PAYMENT_SUCCESS(1, "付款成功"),
    
    /**
     * 余额不足
     */
    INSUFFICIENT_BALANCE(2, "付款失败，余额不足"),
    
    /**
     * 账号或币种错误
     */
    ACCOUNT_OR_CURRENCY_ERROR(3, "付款失败，账号或币种错误"),

    /**
     * 取消付款
     */
    CANCEL_PAYMENT(4, "付款取消");

    private final Integer code;
    private final String message;
    
    PaymentStatusResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据代码获取枚举
     */
    public static PaymentStatusResultEnum getByCode(Integer code) {
        for (PaymentStatusResultEnum enumValue : values()) {
            if (enumValue.code.equals(code)) {
                return enumValue;
            }
        }
        return null;
    }
}