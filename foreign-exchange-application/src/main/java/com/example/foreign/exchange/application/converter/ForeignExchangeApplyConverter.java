package com.example.foreign.exchange.application.converter;

import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyExcelVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyResponseVO;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.aggregate.ForeignExchangeApplyAggregate;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import com.example.foreign.exchange.domain.enums.ApplyStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 外汇申请转换器
 */
public class ForeignExchangeApplyConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 将DTO转换为聚合根
     * @param vo 外汇申请草稿DTO
     * @return 外汇申请聚合根
     */
    public static ForeignExchangeApplyAggregate foreignExchangeApplyDraftRequestVOToAggregate(ForeignExchangeApplyRequestVO vo) {
        // 将VO转换为领域实体
        ForeignExchangeApply foreignExchangeApply = foreignExchangeApplyDraftRequestVOToPO(vo);
        // 创建并返回聚合根
        return new ForeignExchangeApplyAggregate(foreignExchangeApply);
    }

    /**
     * 将VO转换为领域实体
     */
    public static ForeignExchangeApply foreignExchangeApplyDraftRequestVOToPO(ForeignExchangeApplyRequestVO vo) {
        // 将DTO转换为领域实体
        ForeignExchangeApply foreignExchangeApply = new ForeignExchangeApply();
        foreignExchangeApply.setUserId(vo.getUserId());
        foreignExchangeApply.setDirection(vo.getDirection());
        foreignExchangeApply.setCurrency(vo.getCurrency());
        foreignExchangeApply.setAmount(vo.getAmount());
        foreignExchangeApply.setRate(vo.getRate());
        foreignExchangeApply.setRmbAmount(vo.getRmbAmount());
        foreignExchangeApply.setTransactionSubject(vo.getTransactionSubject());
        foreignExchangeApply.setSubjectAccountNo(vo.getSubjectAccountNo());
        foreignExchangeApply.setPurpose(vo.getPurpose());
        foreignExchangeApply.setCounterparty(vo.getCounterparty());
        foreignExchangeApply.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        foreignExchangeApply.setSwiftBic(vo.getSwiftBic());

        // 将List转换为JSON字符串
        try {
            if (vo.getAttachPaths() != null) {
                String attachPathsJson = objectMapper.writeValueAsString(vo.getAttachPaths());
                foreignExchangeApply.setAttachPaths(attachPathsJson);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert attachPaths to JSON", e);
        }

        // 设置草稿标识
        foreignExchangeApply.setIsDraft(vo.getIsDraft());

        // 创建并返回聚合根
        return foreignExchangeApply;
    }
    
    /**
     * 将 ForeignExchangeApply 实体转换为 ForeignExchangeApplyResponseVO
     */
    public static ForeignExchangeApplyResponseVO foreignExchangeApplyToResponseVO(ForeignExchangeApply foreignExchangeApply) {
        if (foreignExchangeApply == null) {
            return null;
        }
        
        ForeignExchangeApplyResponseVO vo = new ForeignExchangeApplyResponseVO();
        vo.setId(foreignExchangeApply.getId());
        vo.setApplyNo(foreignExchangeApply.getApplyNo());
        vo.setUserId(foreignExchangeApply.getUserId());
        vo.setDirection(foreignExchangeApply.getDirection());
        vo.setCurrency(foreignExchangeApply.getCurrency());
        vo.setAmount(foreignExchangeApply.getAmount());
        vo.setRate(foreignExchangeApply.getRate());
        vo.setRmbAmount(foreignExchangeApply.getRmbAmount());
        vo.setTransactionSubject(foreignExchangeApply.getTransactionSubject());
        vo.setSubjectAccountNo(foreignExchangeApply.getSubjectAccountNo());
        vo.setPurpose(foreignExchangeApply.getPurpose());
        vo.setCounterparty(foreignExchangeApply.getCounterparty());
        vo.setCounterpartyAccountNo(foreignExchangeApply.getCounterpartyAccountNo());
        vo.setSwiftBic(foreignExchangeApply.getSwiftBic());
        
        // 将 JSON 字符串转换为 List
        try {
            if (foreignExchangeApply.getAttachPaths() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                java.util.List<String> attachPaths = objectMapper.readValue(foreignExchangeApply.getAttachPaths(), java.util.List.class);
                vo.setAttachPaths(attachPaths);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert attachPaths from JSON", e);
        }
        
        vo.setStatus(foreignExchangeApply.getStatus());
        vo.setSubmitTime(foreignExchangeApply.getSubmitTime());
        vo.setCreateTime(foreignExchangeApply.getCreateTime());
        vo.setIsDraft(foreignExchangeApply.getIsDraft());
        
        return vo;
    }

    /**
     * 将 ForeignExchangeApplyEditRequestVO 转换为 ForeignExchangeApply
     */
    public static ForeignExchangeApply foreignExchangeApplyEditRequestVOToPO(ForeignExchangeApplyEditRequestVO vo) {
        // 1. 判空处理
        if (vo == null) {
            return null;
        }

        // 2. 初始化实体对象
        ForeignExchangeApply foreignExchangeApply = new ForeignExchangeApply();

        // 3. 字段映射
        foreignExchangeApply.setApplyNo(vo.getApplyNo());
        foreignExchangeApply.setUserId(vo.getUserId());
        foreignExchangeApply.setDirection(vo.getDirection());
        foreignExchangeApply.setCurrency(vo.getCurrency());
        foreignExchangeApply.setAmount(vo.getAmount());
        foreignExchangeApply.setRate(vo.getRate());
        foreignExchangeApply.setRmbAmount(vo.getRmbAmount());
        foreignExchangeApply.setTransactionSubject(vo.getTransactionSubject());
        foreignExchangeApply.setSubjectAccountNo(vo.getSubjectAccountNo());
        foreignExchangeApply.setPurpose(vo.getPurpose());
        foreignExchangeApply.setCounterparty(vo.getCounterparty());
        foreignExchangeApply.setCounterpartyAccountNo(vo.getCounterpartyAccountNo());
        foreignExchangeApply.setSwiftBic(vo.getSwiftBic());
//        foreignExchangeApply.setAttachPaths(String.join(",", vo.getAttachPaths()));
        foreignExchangeApply.setIsDraft(vo.getIsDraft());

        // 4. 返回转换后的实体
        return foreignExchangeApply;
    }

    public static Page<ForeignExchangeApplyResponseVO> foreignExchangeApplyPageToResponseVO(Page<ForeignExchangeApply> page) {
        Page<ForeignExchangeApplyResponseVO> responsePage = new Page<>();
        responsePage.setTotal(page.getTotal());
        responsePage.setCurrent(page.getCurrent());
        responsePage.setSize(page.getSize());
        responsePage.setPages(page.getPages());

        // 转换每条记录
        List<ForeignExchangeApplyResponseVO> responseList = new ArrayList<>();
        for (ForeignExchangeApply apply : page.getRecords()) {
            ForeignExchangeApplyResponseVO vo = new ForeignExchangeApplyResponseVO();
            vo.setId(apply.getId());
            vo.setApplyNo(apply.getApplyNo());
            vo.setUserId(apply.getUserId());
            vo.setDirection(apply.getDirection());
            vo.setCurrency(apply.getCurrency());
            vo.setAmount(apply.getAmount());
            vo.setRate(apply.getRate());
            vo.setRmbAmount(apply.getRmbAmount());
            vo.setTransactionSubject(apply.getTransactionSubject());
            vo.setSubjectAccountNo(apply.getSubjectAccountNo());
            vo.setPurpose(apply.getPurpose());
            vo.setCounterparty(apply.getCounterparty());
            vo.setCounterpartyAccountNo(apply.getCounterpartyAccountNo());
            vo.setSwiftBic(apply.getSwiftBic());

            // 将 JSON 字符串转换为 List
            try {
                if (apply.getAttachPaths() != null) {
                    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    java.util.List<String> attachPaths = objectMapper.readValue(apply.getAttachPaths(), java.util.List.class);
                    vo.setAttachPaths(attachPaths);
                }
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException("Failed to convert attachPaths from JSON", e);
            }

            vo.setStatus(apply.getStatus());
            vo.setSubmitTime(apply.getSubmitTime());
            vo.setCreateTime(apply.getCreateTime());
            vo.setIsDraft(apply.getIsDraft());

            responseList.add(vo);
        }
        responsePage.setRecords(responseList);
        return responsePage;
    }

    /**
     * 申请applyVO转化成ExcelVO
     */
    public static ForeignExchangeApplyExcelVO foreignExchangeApplyResponseVOToExcelVO(ForeignExchangeApplyResponseVO apply) {
        ForeignExchangeApplyExcelVO excelVO = new ForeignExchangeApplyExcelVO();
        excelVO.setApplyNo(apply.getApplyNo());
        excelVO.setUserId(apply.getUserId());
        excelVO.setDirection(apply.getDirection() == 1 ? "购汇" : "结汇");
        excelVO.setCurrency(apply.getCurrency());
        excelVO.setAmount(apply.getAmount());
        excelVO.setRate(apply.getRate());
        excelVO.setRmbAmount(apply.getRmbAmount());
        excelVO.setTransactionSubject(apply.getTransactionSubject());
        excelVO.setSubjectAccountNo(apply.getSubjectAccountNo());
        excelVO.setPurpose(apply.getPurpose());
        excelVO.setCounterparty(apply.getCounterparty());
        excelVO.setCounterpartyAccountNo(apply.getCounterpartyAccountNo());
        excelVO.setSwiftBic(apply.getSwiftBic());
        excelVO.setStatus(Objects.requireNonNull(ApplyStatusEnum.getByCode(apply.getStatus())).getMessage());
        excelVO.setSubmitTime(apply.getSubmitTime());
        excelVO.setCreateTime(apply.getCreateTime());
        return excelVO;
    }

}