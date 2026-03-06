package com.example.foreign.exchange.domain.service;

import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;
import com.example.foreign.exchange.domain.enums.PaymentStatusResultEnum;

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
    
    /**
     * 更新付款单状态
     * @param paymentNo 付款单号
     * @param status 状态
     * @param failureReason 失败原因
     * @return 是否更新成功
     */
    boolean updatePaymentStatus(String paymentNo, Integer status, String failureReason);
    
    /**
     * 处理支付状态变更
     * @param paymentNo 付款单号
     * @param subjectAccountNo 交易主体账号
     * @param payCurrency 付款币种
     * @param paymentAmount 付款金额
     * @return 支付状态结果枚举
     */
    PaymentStatusResultEnum handlePaymentStatusChange(String paymentNo, String subjectAccountNo, String payCurrency, java.math.BigDecimal paymentAmount);
    
    /**
     * 取消付款单
     * @param paymentNo 付款单号
     * @return 付款单号
     */
    String cancelPayment(String paymentNo);
}