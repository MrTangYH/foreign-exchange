package com.example.foreign.exchange.controller.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteResponseVO;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteQueryRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 将ForeignExchangeExecuteQueryRequestDTO转换为ForeignExchangeExecuteQueryRequestVO
     */
    public static ForeignExchangeExecuteQueryRequestVO foreignExchangeExecuteQueryRequestDTO2VO(ForeignExchangeExecuteQueryRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        ForeignExchangeExecuteQueryRequestVO vo = new ForeignExchangeExecuteQueryRequestVO();
        vo.setOrderNo(dto.getOrderNo());
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
        vo.setPage(dto.getPage());
        vo.setSize(dto.getSize());

        return vo;
    }

    /**
     * 将ForeignExchangeExecuteResponseVO转换为ForeignExchangeExecuteResponseDTO
     */
    public static ForeignExchangeExecuteResponseDTO foreignExchangeExecuteResponseVO2DTO(ForeignExchangeExecuteResponseVO vo) {
        if (vo == null) {
            return null;
        }

        ForeignExchangeExecuteResponseDTO dto = new ForeignExchangeExecuteResponseDTO();
        dto.setId(vo.getId());
        dto.setOrderNo(vo.getOrderNo());
        dto.setApplyNo(vo.getApplyNo());
        dto.setCurrency(vo.getCurrency());
        dto.setAmount(vo.getAmount());
        dto.setRmbAmount(vo.getRmbAmount());
        dto.setRate(vo.getRate());
        dto.setTransactionSubject(vo.getTransactionSubject());
        dto.setSubjectAccountNo(vo.getSubjectAccountNo());
        dto.setCounterparty(vo.getCounterparty());
        dto.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        dto.setSwiftBic(vo.getSwiftBic());
        dto.setStatus(vo.getStatus());
        dto.setUserId(vo.getUserId());
        dto.setCreateTime(vo.getCreateTime());

        return dto;
    }

    /**
     * 将Page<ForeignExchangeExecuteResponseVO>转换为Page<ForeignExchangeExecuteResponseDTO>
     */
    public static Page<ForeignExchangeExecuteResponseDTO> foreignExchangeExecuteResponseVOPage2DTOPage(Page<ForeignExchangeExecuteResponseVO> voPage) {
        if (voPage == null) {
            return null;
        }

        // 转换记录列表
        List<ForeignExchangeExecuteResponseDTO> dtoList = voPage.getRecords().stream()
                .map(ForeignExchangeExecuteConverter::foreignExchangeExecuteResponseVO2DTO)
                .collect(Collectors.toList());

        // 构建新的分页对象
        Page<ForeignExchangeExecuteResponseDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setTotal(voPage.getTotal());
        dtoPage.setPages(voPage.getPages());
        dtoPage.setSize(voPage.getSize());
        dtoPage.setCurrent(voPage.getCurrent());

        return dtoPage;
    }
}