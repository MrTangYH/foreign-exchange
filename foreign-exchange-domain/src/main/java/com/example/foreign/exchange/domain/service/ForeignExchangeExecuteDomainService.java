package com.example.foreign.exchange.domain.service;

import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;

/**
 * 外汇执行单领域服务
 */
public interface ForeignExchangeExecuteDomainService {
    /**
     * 保存执行单
     * @param order 执行单实体
     * @return 执行单号
     */
    String saveExecuteOrder(ForeignExchangeOrder order);
}