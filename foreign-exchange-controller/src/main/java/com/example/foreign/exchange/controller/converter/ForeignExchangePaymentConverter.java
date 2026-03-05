package com.example.foreign.exchange.controller.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentResponseVO;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentQueryRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外汇付款转换器
 */
public class ForeignExchangePaymentConverter {
    
    /**
     * 将ForeignExchangePaymentRequestDTO转换为ForeignExchangePaymentRequestVO
     */
    public static ForeignExchangePaymentRequestVO foreignExchangePaymentRequestDTO2VO(ForeignExchangePaymentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ForeignExchangePaymentRequestVO vo = new ForeignExchangePaymentRequestVO();
        vo.setOrderNo(dto.getOrderNo());
        vo.setSubjectAccountNo(dto.getSubjectAccountNo());
        vo.setCounterpartyAccountNo(dto.getCounterpartyAccountNo());
        vo.setSwiftBic(dto.getSwiftBic());
        vo.setStatus(1); // 默认状态为待付款
        vo.setUserId(dto.getUserId());
        vo.setCreateTime(dto.getCreateTime());
        
        // 根据direction设置币种和金额
        if (dto.getDirection() != null) {
            if (dto.getDirection() == 1) {
                // 购汇：付款币种为RMB，收款币种为DTO中的currency
                vo.setPayCurrency("RMB");
                vo.setPaymentAmount(dto.getRmbAmount());
                vo.setReceivedCurrency(dto.getCurrency());
                vo.setReceivedAmount(dto.getAmount());
            } else if (dto.getDirection() == 2) {
                // 结汇：收款币种为RMB，付款币种为DTO中的currency
                vo.setPayCurrency(dto.getCurrency());
                vo.setPaymentAmount(dto.getAmount());
                vo.setReceivedCurrency("RMB");
                vo.setReceivedAmount(dto.getRmbAmount());
            }
        }
        
        return vo;
    }
    
    /**
     * 将ForeignExchangePaymentQueryRequestDTO转换为ForeignExchangePaymentQueryRequestVO
     */
    public static ForeignExchangePaymentQueryRequestVO foreignExchangePaymentQueryRequestDTO2VO(ForeignExchangePaymentQueryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ForeignExchangePaymentQueryRequestVO vo = new ForeignExchangePaymentQueryRequestVO();
        vo.setPaymentNo(dto.getPaymentNo());
        vo.setPaymentAmountMin(dto.getPaymentAmountMin());
        vo.setPaymentAmountMax(dto.getPaymentAmountMax());
        vo.setReceivedAmountMin(dto.getReceivedAmountMin());
        vo.setReceivedAmountMax(dto.getReceivedAmountMax());
        vo.setCreateTimeStart(dto.getCreateTimeStart());
        vo.setCreateTimeEnd(dto.getCreateTimeEnd());
        vo.setPage(dto.getPage());
        vo.setSize(dto.getSize());
        
        return vo;
    }
    
    /**
     * 将ForeignExchangePaymentResponseVO转换为ForeignExchangePaymentResponseDTO
     */
    public static ForeignExchangePaymentResponseDTO foreignExchangePaymentResponseVO2DTO(ForeignExchangePaymentResponseVO vo) {
        if (vo == null) {
            return null;
        }
        
        ForeignExchangePaymentResponseDTO dto = new ForeignExchangePaymentResponseDTO();
        dto.setId(vo.getId());
        dto.setPaymentNo(vo.getPaymentNo());
        dto.setOrderNo(vo.getOrderNo());
        dto.setPaymentAmount(vo.getPaymentAmount());
        dto.setPayCurrency(vo.getPayCurrency());
        dto.setReceivedAmount(vo.getReceivedAmount());
        dto.setReceivedCurrency(vo.getReceivedCurrency());
        dto.setSubjectAccountNo(vo.getSubjectAccountNo());
        dto.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        dto.setSwiftBic(vo.getSwiftBic());
        dto.setStatus(vo.getStatus());
        dto.setPaymentTime(vo.getPaymentTime());
        dto.setFailureReason(vo.getFailureReason());
        dto.setUserId(vo.getUserId());
        dto.setCreateTime(vo.getCreateTime());
        
        return dto;
    }
    
    /**
     * 将Page<ForeignExchangePaymentResponseVO>转换为Page<ForeignExchangePaymentResponseDTO>
     */
    public static Page<ForeignExchangePaymentResponseDTO> foreignExchangePaymentResponseVOPage2DTOPage(Page<ForeignExchangePaymentResponseVO> voPage) {
        if (voPage == null) {
            return null;
        }
        
        // 转换记录列表
        List<ForeignExchangePaymentResponseDTO> dtoList = voPage.getRecords().stream()
                .map(ForeignExchangePaymentConverter::foreignExchangePaymentResponseVO2DTO)
                .collect(Collectors.toList());
        
        // 构建新的分页对象
        Page<ForeignExchangePaymentResponseDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setTotal(voPage.getTotal());
        dtoPage.setPages(voPage.getPages());
        dtoPage.setSize(voPage.getSize());
        dtoPage.setCurrent(voPage.getCurrent());
        
        return dtoPage;
    }
}