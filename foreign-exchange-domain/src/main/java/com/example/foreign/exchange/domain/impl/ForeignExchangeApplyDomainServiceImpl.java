package com.example.foreign.exchange.domain.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.aggregate.ForeignExchangeApplyAggregate;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import com.example.foreign.exchange.domain.enums.ApplyStatusEnum;
import com.example.foreign.exchange.domain.enums.DraftStatusEnum;
import com.example.foreign.exchange.domain.repository.ForeignExchangeApplyRepository;
import com.example.foreign.exchange.domain.service.ForeignExchangeApplyDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("ForeignExchangeApplyDomainService")
public class ForeignExchangeApplyDomainServiceImpl implements ForeignExchangeApplyDomainService {
    @Resource
    private RedisIdGenerator redisIdGenerator;

    @Resource
    private ForeignExchangeApplyRepository foreignExchangeApplyRepository;

    @Override
    public String saveDraft(ForeignExchangeApplyAggregate aggregate) {

        // 保存到数据库
        foreignExchangeApplyRepository.insert(aggregate.getForeignExchangeApply());
        return aggregate.getForeignExchangeApply().getApplyNo();
    }

    @Override
    public Page<ForeignExchangeApply> queryApplyList(ForeignExchangeApply request, Long page, Long size) {
        // 构建查询条件
        QueryWrapper<ForeignExchangeApply> queryWrapper = new QueryWrapper<>();
        
        // 添加筛选条件
        // 管理员代号为1，可以看到所有申请记录
        if (request.getUserId() != null && !request.getUserId().equals(1L)) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (request.getDirection() != null) {
            queryWrapper.eq("direction", request.getDirection());
        }
        if (request.getCurrency() != null && !request.getCurrency().isEmpty()) {
            queryWrapper.eq("currency", request.getCurrency());
        }
        if (request.getIsDraft() != null) {
            queryWrapper.eq("is_draft", request.getIsDraft());
        }
        if (request.getApplyNo() != null && !request.getApplyNo().isEmpty()) {
            queryWrapper.eq("apply_no", request.getApplyNo());
        }
        
        // 分页查询，使用传递过来的分页参数
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ForeignExchangeApply> pageObj = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        IPage<ForeignExchangeApply> result = foreignExchangeApplyRepository.selectPage(pageObj, queryWrapper);
        
        // 构建返回结果
        Page<ForeignExchangeApply> pageResult = new Page<>();
        pageResult.setRecords(result.getRecords());
        pageResult.setTotal(result.getTotal());
        pageResult.setCurrent(result.getCurrent());
        pageResult.setSize(result.getSize());
        pageResult.setPages(result.getPages());
        
        return pageResult;
    }

    @Override
    public String editDraft(ForeignExchangeApply foreignExchangeApply) {
        // 根据apply_no查询
        QueryWrapper<ForeignExchangeApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apply_no", foreignExchangeApply.getApplyNo());
        ForeignExchangeApply existingApply = foreignExchangeApplyRepository.selectOne(queryWrapper);
        if (existingApply == null) {
            throw new RuntimeException("草稿不存在");
        }
        if (!existingApply.getStatus().equals(ApplyStatusEnum.DRAFT.getCode())) {
            throw new RuntimeException("非草稿状态，不能编辑");
        }
        // 更新数据
        foreignExchangeApply.setId(existingApply.getId()); // 保留原ID
        foreignExchangeApply.setCreateTime(existingApply.getCreateTime()); // 保留创建时间
        foreignExchangeApply.setSubmitTime(LocalDateTime.now()); // 更新提交时间
        foreignExchangeApplyRepository.updateById(foreignExchangeApply);
        return foreignExchangeApply.getApplyNo();
    }

    @Override
    public String changeStatus(String applyNo, Integer status) {
        // 根据apply_no查询
        QueryWrapper<ForeignExchangeApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("apply_no", applyNo);
        ForeignExchangeApply existingApply = foreignExchangeApplyRepository.selectOne(queryWrapper);
        if (existingApply == null) {
            throw new RuntimeException("申请不存在");
        }

        // 根据状态码执行不同的逻辑
        ApplyStatusEnum targetStatus = ApplyStatusEnum.getByCode(status);
        if (targetStatus == null) {
            throw new RuntimeException("无效的状态码");
        }

        switch (targetStatus) {
            case SUBMITTED:
                if (existingApply.getIsDraft() == DraftStatusEnum.DRAFT.getCode()) {
                    existingApply.setStatus(ApplyStatusEnum.SUBMITTED.getCode());
                    existingApply.setIsDraft(DraftStatusEnum.NOT_DRAFT.getCode());
                    existingApply.setSubmitTime(LocalDateTime.now());
                } else {
                    throw new RuntimeException("只有草稿状态才能提交");
                }
                break;
            case CANCELLED:
                if (existingApply.getStatus() == ApplyStatusEnum.SUBMITTED.getCode() &&
                        existingApply.getIsDraft() == DraftStatusEnum.NOT_DRAFT.getCode()) {
                    existingApply.setStatus(ApplyStatusEnum.CANCELLED.getCode());
                    existingApply.setIsDraft(DraftStatusEnum.DRAFT.getCode());
                } else {
                    throw new RuntimeException("只有已提交状态才能取消");
                }
                break;
            case APPROVED:
                if (existingApply.getStatus() == ApplyStatusEnum.SUBMITTED.getCode() &&
                        existingApply.getIsDraft() == DraftStatusEnum.NOT_DRAFT.getCode()) {
                    existingApply.setStatus(ApplyStatusEnum.APPROVED.getCode());
                } else {
                    throw new RuntimeException("只有已提交状态才能通过");
                }
                break;
            case REJECTED:
                if (existingApply.getStatus() == ApplyStatusEnum.SUBMITTED.getCode() &&
                        existingApply.getIsDraft() == DraftStatusEnum.NOT_DRAFT.getCode()) {
                    existingApply.setStatus(ApplyStatusEnum.REJECTED.getCode());
                    existingApply.setIsDraft(DraftStatusEnum.DRAFT.getCode());
                } else {
                    throw new RuntimeException("只有已提交状态才能驳回");
                }
                break;
            default:
                throw new RuntimeException("无效的状态码");
        }

        // 更新数据
        foreignExchangeApplyRepository.updateById(existingApply);
        return applyNo;
    }
}