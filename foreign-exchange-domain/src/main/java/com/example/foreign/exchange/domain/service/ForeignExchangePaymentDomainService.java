package com.example.foreign.exchange.domain.service;

import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;

/**
 * 外汇付款领域服务
 */
public interface ForeignExchangePaymentDomainService {
    /**
     * 保存付款单
     * @param payment 付款单实体
     * @return 付款单号
     */
    String savePaymentOrder(ForeignExchangePayment payment);
    
    /**
     * 查询付款单列表
     * @param query 查询条件
     * @return 付款单列表
     */
    Page<ForeignExchangePayment> queryPaymentOrderList(ForeignExchangePaymentQuery query);
}