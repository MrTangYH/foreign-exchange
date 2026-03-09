package com.example.foreign.exchange.application.converter;

import com.example.foreign.exchange.application.entity.*;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import com.example.foreign.exchange.domain.enums.ExecuteStatusEnum;

import java.util.Objects;

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
        execute.setDirection(vo.getDirection());
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
        execute.setDirection(vo.getDirection());
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
     * 执行executeVO转化成ExcelVO
     */
    public static ForeignExchangeExecuteExcelVO foreignExchangeExecuteResponseVOToExcelVO(ForeignExchangeExecuteResponseVO execute) {
        ForeignExchangeExecuteExcelVO excelVO = new ForeignExchangeExecuteExcelVO();
        excelVO.setExecuteNo(execute.getOrderNo());
        excelVO.setApplyNo(execute.getApplyNo());
        excelVO.setDirection(execute.getDirection() == 1 ? "购汇" : "结汇");
        excelVO.setCurrency(execute.getCurrency());
        excelVO.setExecuteAmount(execute.getAmount());
        excelVO.setExecuteRate(execute.getRate());
        excelVO.setExecuteTime(execute.getCreateTime());
        excelVO.setStatus(Objects.requireNonNull(ExecuteStatusEnum.getByCode(execute.getStatus())).getMessage());
        excelVO.setCreateTime(execute.getCreateTime());
        return excelVO;
    }
}