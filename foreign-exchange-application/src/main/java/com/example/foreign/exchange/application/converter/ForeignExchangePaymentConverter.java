package com.example.foreign.exchange.application.converter;

import com.example.foreign.exchange.application.entity.*;
import com.example.foreign.exchange.domain.enums.PaymentStatusResultEnum;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;

import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;

import java.util.Objects;

/**
 * 外汇付款转换器
 */
public class ForeignExchangePaymentConverter {
    
    /**
     * 将ForeignExchangePaymentQueryRequestVO转换为ForeignExchangePaymentQuery
     */
    public static ForeignExchangePaymentQuery foreignExchangePaymentQueryRequestVOToQuery(ForeignExchangePaymentQueryRequestVO vo) {
        if (vo == null) {
            return null;
        }
        
        ForeignExchangePaymentQuery query = new ForeignExchangePaymentQuery();
        query.setPaymentNo(vo.getPaymentNo());
        query.setPaymentAmountMin(vo.getPaymentAmountMin());
        query.setPaymentAmountMax(vo.getPaymentAmountMax());
        query.setReceivedAmountMin(vo.getReceivedAmountMin());
        query.setReceivedAmountMax(vo.getReceivedAmountMax());
        query.setCreateTimeStart(vo.getCreateTimeStart());
        query.setCreateTimeEnd(vo.getCreateTimeEnd());
        query.setPage(vo.getPage());
        query.setSize(vo.getSize());
        
        return query;
    }

    /**
     * 付款paymenyVO转化成ExcelVO
     */
    public static ForeignExchangePaymentExcelVO foreignExchangePaymentResponseVOToExcelVO(ForeignExchangePaymentResponseVO payment) {
        ForeignExchangePaymentExcelVO excelVO = new ForeignExchangePaymentExcelVO();
        excelVO.setPaymentNo(payment.getPaymentNo());
        excelVO.setExecuteNo(payment.getOrderNo());
//                excelVO.setDirection(payment.get() == 1 ? "购汇" : "结汇");
        excelVO.setCurrency(payment.getPayCurrency());
        excelVO.setPaymentAmount(payment.getPaymentAmount());
        excelVO.setSubjectAccountNo(payment.getSubjectAccountNo());
        excelVO.setCounterpartyAccountNo(payment.getCounterpartyAccountNo());
        excelVO.setPaymentTime(payment.getPaymentTime());
        excelVO.setStatus(Objects.requireNonNull(PaymentStatusResultEnum.getByCode(payment.getStatus())).getMessage());
        excelVO.setCreateTime(payment.getCreateTime());
        return excelVO;
    }
}