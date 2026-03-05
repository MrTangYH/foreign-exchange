package com.example.foreign.exchange.application.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;

/**
 * 外汇执行单转换器
 */
public class ForeignExchangeExecuteConverter {
    
    /**
     * 将ForeignExchangeExecuteRequestVO转换为ForeignExchangeExecute实体
     */
    public static ForeignExchangeOrder foreignExchangeExecuteRequestVOToPO(ForeignExchangeExecuteRequestVO vo) {
        if (vo == null) {
            return null;
        }
        
        ForeignExchangeOrder execute = new ForeignExchangeOrder();
        execute.setApplyNo(vo.getApplyNo());
        execute.setCurrency(vo.getCurrency());
        execute.setAmount(vo.getAmount());
        execute.setRmbAmount(vo.getRmbAmount());
        execute.setRate(vo.getRate());
        execute.setTransactionSubject(vo.getTransactionSubject());
        execute.setSubjectAccountNo(vo.getSubjectAccountNo());
        execute.setCounterparty(vo.getCounterparty());
        execute.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        execute.setSwiftBic(vo.getSwiftBic());
        execute.setStatus(vo.getStatus());
        execute.setUserId(vo.getUserId());
        
        return execute;
    }
    
    /**
     * 将ForeignExchangeExecuteQueryRequestVO转换为ForeignExchangeExecute实体
     */
    public static ForeignExchangeOrder foreignExchangeExecuteQueryRequestVOToPO(ForeignExchangeExecuteQueryRequestVO vo) {
        if (vo == null) {
            return null;
        }
        
        ForeignExchangeOrder execute = new ForeignExchangeOrder();
        execute.setOrderNo(vo.getOrderNo());
        execute.setApplyNo(vo.getApplyNo());
        execute.setCurrency(vo.getCurrency());
        execute.setAmount(vo.getAmount());
        execute.setRmbAmount(vo.getRmbAmount());
        execute.setRate(vo.getRate());
        execute.setTransactionSubject(vo.getTransactionSubject());
        execute.setSubjectAccountNo(vo.getSubjectAccountNo());
        execute.setCounterparty(vo.getCounterparty());
        execute.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        execute.setSwiftBic(vo.getSwiftBic());
        execute.setStatus(vo.getStatus());
        execute.setUserId(vo.getUserId());
        
        return execute;
    }
}