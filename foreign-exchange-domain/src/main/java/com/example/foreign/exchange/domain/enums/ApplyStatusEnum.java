package com.example.foreign.exchange.domain.enums;

import lombok.Getter;

@Getter
public enum ApplyStatusEnum {
    
    DRAFT(1, "草稿"),
    SUBMITTED(0, "已提交"),
    APPROVED(2, "已通过"),
    CANCELLED(3, "已取消"),
    REJECTED(4, "已驳回");
    
    private final Integer code;
    private final String message;
    
    ApplyStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 根据状态码获取枚举
     */
    public static ApplyStatusEnum getByCode(Integer code) {
        for (ApplyStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}