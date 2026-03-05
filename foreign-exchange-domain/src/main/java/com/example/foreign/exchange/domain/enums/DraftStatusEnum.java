package com.example.foreign.exchange.domain.enums;

import lombok.Getter;

@Getter
public enum DraftStatusEnum {

    DRAFT(1, "草稿"),
    NOT_DRAFT(0, "非草稿");

    private final Integer code;
    private final String message;

    DraftStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     */
    public static DraftStatusEnum getByCode(Integer code) {
        for (DraftStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}