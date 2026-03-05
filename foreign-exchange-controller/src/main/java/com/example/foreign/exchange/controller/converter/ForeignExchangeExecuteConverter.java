package com.example.foreign.exchange.controller.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteRequestDTO;

/**
 * 外汇执行单转换器
 */
public class ForeignExchangeExecuteConverter {
    
    /**
     * 将ForeignExchangeExecuteRequestDTO转换为ForeignExchangeExecuteRequestVO
     */
    public static ForeignExchangeExecuteRequestVO foreignExchangeExecuteRequestDTO2VO(ForeignExchangeExecuteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ForeignExchangeExecuteRequestVO vo = new ForeignExchangeExecuteRequestVO();
        vo.setApplyNo(dto.getApplyNo());
        vo.setCurrency(dto.getCurrency());
        vo.setAmount(dto.getAmount());
        vo.setRmbAmount(dto.getRmbAmount());
        vo.setRate(dto.getRate());
        vo.setTransactionSubject(dto.getTransactionSubject());
        vo.setSubjectAccountNo(dto.getSubjectAccountNo());
        vo.setCounterparty(dto.getCounterparty());
        vo.setCounterpartyAccountNo(dto.getCounterpartyAccountNo());
        vo.setSwiftBic(dto.getSwiftBic());
        vo.setStatus(dto.getStatus());
        vo.setUserId(dto.getUserId());
        
        return vo;
    }
}