package com.example.foreign.exchange.domain.Impl;

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
}