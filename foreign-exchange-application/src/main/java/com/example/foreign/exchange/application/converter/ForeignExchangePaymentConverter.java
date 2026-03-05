package com.example.foreign.exchange.application.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;

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
}