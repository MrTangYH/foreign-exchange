package com.example.foreign.exchange.domain.service;

import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.aggregate.ForeignExchangeApplyAggregate;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;

/**
 * 外汇申请领域服务
 */
public interface ForeignExchangeApplyDomainService {
    /**
     * 保存外汇申请草稿
     * @param aggregate 外汇申请聚合根
     * @return 保存后的外汇申请聚合根
     */
    String saveDraft(ForeignExchangeApplyAggregate aggregate);
    
    /**
     * 查询外汇申请列表
     * @param request 查询条件
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果，包含列表数据和分页信息
     */
    Page<ForeignExchangeApply> queryApplyList(ForeignExchangeApply request, Long page, Long size);

    /**
     * 编辑外汇申请草稿
     * @param foreignExchangeApply 外汇申请实体
     * @return 申请编号
     */
    String editDraft(ForeignExchangeApply foreignExchangeApply);

    /**
     * 变更外汇申请状态
     * @param applyNo 申请编号
     * @param status 状态码
     * @return 申请编号
     */
    String changeStatus(String applyNo, Integer status);
}