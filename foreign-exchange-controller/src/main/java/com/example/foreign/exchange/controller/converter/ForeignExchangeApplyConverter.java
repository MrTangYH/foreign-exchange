package com.example.foreign.exchange.controller.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyResponseVO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeApplyEditRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeApplyRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeApplyResponseDTO;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ForeignExchangeApplyConverter {


    public static ForeignExchangeApplyRequestVO foreignExchangeApplyRequestDTO2VO(ForeignExchangeApplyRequestDTO dto) {
        // 1. 判空处理（避免空指针异常）
        if (dto == null) {
            return null;
        }

        // 2. 初始化VO对象
        ForeignExchangeApplyRequestVO vo = new ForeignExchangeApplyRequestVO();

        // 3. 基础字段映射（类型完全一致）
        vo.setUserId(dto.getUserId());
        vo.setDirection(dto.getDirection());
        vo.setCurrency(dto.getCurrency());
        vo.setAmount(dto.getAmount());
        vo.setRate(dto.getRate());
        vo.setRmbAmount(dto.getRmbAmount());

        // 4. 交易主体相关字段
        vo.setTransactionSubject(dto.getTransactionSubject());
        vo.setSubjectAccountNo(dto.getSubjectAccountNo());

        // 5. 资金用途、对手方信息
        vo.setPurpose(dto.getPurpose());
        vo.setCounterparty(dto.getCounterparty());
        vo.setCounterpartyAccountNo(dto.getCounterpartyAccountNo());
        vo.setSwiftBic(dto.getSwiftBic());
        vo.setAttachPaths(dto.getAttachPaths());// 7. 草稿标识
        vo.setIsDraft(dto.getIsDraft());
        
        // 8. 分页参数
        vo.setPage(dto.getPage());
        vo.setSize(dto.getSize());

        // 9. 返回转化后的VO
        return vo;
    }
    
    /**
     * 将 ForeignExchangeApplyVO 实体转换为 ForeignExchangeApplyResponseDTO
     */
    public static ForeignExchangeApplyResponseDTO foreignExchangeApplyToResponseDTO(ForeignExchangeApplyResponseVO foreignExchangeApply) {
        if (foreignExchangeApply == null) {
            return null;
        }
        
        ForeignExchangeApplyResponseDTO dto = new ForeignExchangeApplyResponseDTO();
        dto.setId(foreignExchangeApply.getId());
        dto.setApplyNo(foreignExchangeApply.getApplyNo());
        dto.setUserId(foreignExchangeApply.getUserId());
        dto.setDirection(foreignExchangeApply.getDirection());
        dto.setCurrency(foreignExchangeApply.getCurrency());
        dto.setAmount(foreignExchangeApply.getAmount());
        dto.setRate(foreignExchangeApply.getRate());
        dto.setRmbAmount(foreignExchangeApply.getRmbAmount());
        dto.setTransactionSubject(foreignExchangeApply.getTransactionSubject());
        dto.setSubjectAccountNo(foreignExchangeApply.getSubjectAccountNo());
        dto.setPurpose(foreignExchangeApply.getPurpose());
        dto.setCounterparty(foreignExchangeApply.getCounterparty());
        dto.setCounterpartyAccountNo(foreignExchangeApply.getCounterpartyAccountNo());
        dto.setSwiftBic(foreignExchangeApply.getSwiftBic());
        
//        // 将 JSON 字符串转换为 List
//        try {
//            if (foreignExchangeApply.getAttachPaths() != null) {
//                ObjectMapper objectMapper = new ObjectMapper();
//                java.util.List<String> attachPaths = objectMapper.readValue(foreignExchangeApply.getAttachPaths(), java.util.List.class);
//                dto.setAttachPaths(attachPaths);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to convert attachPaths from JSON", e);
//        }
        
        dto.setStatus(foreignExchangeApply.getStatus());
        dto.setSubmitTime(foreignExchangeApply.getSubmitTime());
        dto.setCreateTime(foreignExchangeApply.getCreateTime());
        dto.setIsDraft(foreignExchangeApply.getIsDraft());
        
        return dto;
    }

    /**
     * 将 ForeignExchangeApplyEditRequestDTO 转换为 ForeignExchangeApplyEditRequestVO
     */
    public static ForeignExchangeApplyEditRequestVO foreignExchangeApplyEditRequestDTO2VO(ForeignExchangeApplyEditRequestDTO dto) {
        // 1. 判空处理
        if (dto == null) {
            return null;
        }

        // 2. 初始化VO对象
        ForeignExchangeApplyEditRequestVO vo = new ForeignExchangeApplyEditRequestVO();

        // 3. 基础字段映射
        vo.setApplyNo(dto.getApplyNo());
        vo.setUserId(dto.getUserId());
        vo.setDirection(dto.getDirection());
        vo.setCurrency(dto.getCurrency());
        vo.setAmount(dto.getAmount());
        vo.setRate(dto.getRate());
        vo.setRmbAmount(dto.getRmbAmount());
        vo.setTransactionSubject(dto.getTransactionSubject());
        vo.setSubjectAccountNo(dto.getSubjectAccountNo());
        vo.setPurpose(dto.getPurpose());
        vo.setCounterparty(dto.getCounterparty());
        vo.setCounterpartyAccountNo(dto.getCounterpartyAccountNo());
        vo.setSwiftBic(dto.getSwiftBic());
        vo.setAttachPaths(dto.getAttachPaths());
        vo.setIsDraft(dto.getIsDraft());
        vo.setPage(dto.getPage());
        vo.setSize(dto.getSize());

        // 4. 返回转化后的VO
        return vo;
    }
}