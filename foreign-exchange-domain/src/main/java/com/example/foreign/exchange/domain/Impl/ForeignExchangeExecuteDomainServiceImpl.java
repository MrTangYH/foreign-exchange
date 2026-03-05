package com.example.foreign.exchange.domain.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import com.example.foreign.exchange.domain.repository.ForeignExchangeOrderRepository;
import com.example.foreign.exchange.domain.service.ForeignExchangeExecuteDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 外汇执行单领域服务实现
 */
@Service("ForeignExchangeExecuteDomainService")
public class ForeignExchangeExecuteDomainServiceImpl implements ForeignExchangeExecuteDomainService {
    
    @Resource
    private ForeignExchangeOrderRepository foreignExchangeOrderRepository;
    
    @Override
    public String saveExecuteOrder(ForeignExchangeOrder order) {
        // 设置创建时间
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        // 保存到数据库
        foreignExchangeOrderRepository.insert(order);
        
        return order.getOrderNo();
    }

    @Override
    public Page<ForeignExchangeOrder> queryExecuteOrderList(ForeignExchangeOrder execute, Long page, Long size) {
        // 构建查询条件
        QueryWrapper<ForeignExchangeOrder> queryWrapper = new QueryWrapper<>();

        // 添加筛选条件
        if (execute != null) {
            if (execute.getOrderNo() != null && !execute.getOrderNo().isEmpty()) {
                queryWrapper.eq("order_no", execute.getOrderNo());
            }
            if (execute.getApplyNo() != null && !execute.getApplyNo().isEmpty()) {
                queryWrapper.eq("apply_no", execute.getApplyNo());
            }
            if (execute.getDirection() != null) {
                queryWrapper.eq("direction", execute.getDirection());
            }
            if (execute.getCurrency() != null && !execute.getCurrency().isEmpty()) {
                queryWrapper.eq("currency", execute.getCurrency());
            }
            if (execute.getStatus() != null) {
                queryWrapper.eq("status", execute.getStatus());
            }
            if (execute.getUserId() != null) {
                queryWrapper.eq("user_id", execute.getUserId());
            }
        }

        // 构建分页参数
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ForeignExchangeOrder> pageObj = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);

        // 执行查询
        IPage<ForeignExchangeOrder> resultPage = foreignExchangeOrderRepository.selectPage(pageObj, queryWrapper);

        // 转换为通用分页对象
        Page<ForeignExchangeOrder> commonPage = new Page<>();
        commonPage.setRecords(resultPage.getRecords());
        commonPage.setTotal(resultPage.getTotal());
        commonPage.setCurrent(resultPage.getCurrent());
        commonPage.setSize(resultPage.getSize());
        commonPage.setPages(resultPage.getPages());

        return commonPage;
    }
}