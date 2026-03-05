package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.converter.ForeignExchangeApplyConverter;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyResponseVO;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.aggregate.ForeignExchangeApplyAggregate;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import com.example.foreign.exchange.domain.repository.ForeignExchangeApplyRepository;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.service.ForeignExchangeApplyDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 外汇申请应用服务
 */
@Service
public class ForeignExchangeApplyApplicationService {
    @Resource
    private RedisIdGenerator redisIdGenerator;
    
    @Resource
    private ForeignExchangeApplyDomainService foreignExchangeApplyDomainService;
    
    @Resource
    private ForeignExchangeApplyRepository foreignExchangeApplyRepository;

    public String saveDraft(ForeignExchangeApplyRequestVO request) {
        // 生成申请编号
        String applyNo = redisIdGenerator.generateApplyNo();
        ForeignExchangeApplyAggregate aggregate = ForeignExchangeApplyConverter.foreignExchangeApplyDraftRequestVOToAggregate(request);

        aggregate.getForeignExchangeApply().setApplyNo(applyNo);
        
        // 设置状态为草稿
        aggregate.getForeignExchangeApply().setStatus(1);
        aggregate.getForeignExchangeApply().setIsDraft(1);

        // 6. 附件路径列表（深拷贝，避免引用传递导致的后续修改问题）
//        if (aggregate.getForeignExchangeApply().getAttachPaths() != null) {
//            // 新建List，避免直接赋值导致原DTO列表被修改时VO也跟着变
//            List<String> attachPaths = new ArrayList<>(dto.getAttachPaths());
//            vo.setAttachPaths(attachPaths);
//        } else {
//            // 空列表而非null，避免后续遍历NPE
//            vo.setAttachPaths(new ArrayList<>());
//        }
        
        // 设置创建时间
        if (aggregate.getForeignExchangeApply().getCreateTime() == null) {
            aggregate.getForeignExchangeApply().setCreateTime(LocalDateTime.now());
        }
        
        // 设置提交时间
        aggregate.getForeignExchangeApply().setSubmitTime(LocalDateTime.now());
        
        // 保存到数据库
        return foreignExchangeApplyDomainService.saveDraft(aggregate);
    }

    public Page<ForeignExchangeApplyResponseVO> queryApplyList(ForeignExchangeApplyEditRequestVO request) {
        ForeignExchangeApply foreignExchangeApply = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestVOToPO(request);
        // 调用领域服务查询列表
        Page<ForeignExchangeApply> result = foreignExchangeApplyDomainService.queryApplyList(foreignExchangeApply, request.getPage(), request.getSize());
        
        // 转换数据
        return ForeignExchangeApplyConverter.foreignExchangeApplyPageToResponseVO(result);
    }

    /**
     * 编辑外汇申请草稿
     * @param request 编辑请求
     * @return 申请编号
     */
    public String editDraft(ForeignExchangeApplyEditRequestVO request) {
        // 转换VO为领域实体
        ForeignExchangeApply foreignExchangeApply = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestVOToPO(request);
        // 调用领域服务编辑草稿
        return foreignExchangeApplyDomainService.editDraft(foreignExchangeApply);
    }

    /**
     * 变更外汇申请状态
     * @param applyNo 申请编号
     * @param status 状态码
     * @return 申请编号
     */
    public String changeStatus(String applyNo, Integer status) {
        // 调用领域服务变更状态
        return foreignExchangeApplyDomainService.changeStatus(applyNo, status);
    }
}