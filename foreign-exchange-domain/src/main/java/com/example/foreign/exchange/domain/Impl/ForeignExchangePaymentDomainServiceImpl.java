package com.example.foreign.exchange.domain.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;
import com.example.foreign.exchange.domain.repository.ForeignExchangePaymentRepository;
import com.example.foreign.exchange.domain.service.ForeignExchangePaymentDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 外汇付款领域服务实现
 */
@Service("ForeignExchangePaymentDomainService")
public class ForeignExchangePaymentDomainServiceImpl implements ForeignExchangePaymentDomainService {
    
    @Resource
    private ForeignExchangePaymentRepository foreignExchangePaymentRepository;
    
    @Override
    public String savePaymentOrder(ForeignExchangePayment payment) {
        // 设置创建时间
        if (payment.getCreateTime() == null) {
            payment.setCreateTime(LocalDateTime.now());
        }
        
        // 保存到数据库
        foreignExchangePaymentRepository.insert(payment);
        
        return payment.getPaymentNo();
    }
    
    @Override
    public Page<ForeignExchangePayment> queryPaymentOrderList(ForeignExchangePaymentQuery query) {
        // 构建查询条件
        QueryWrapper<ForeignExchangePayment> queryWrapper = new QueryWrapper<>();
        
        // 添加筛选条件
        if (query.getPaymentNo() != null && !query.getPaymentNo().isEmpty()) {
            queryWrapper.eq("payment_no", query.getPaymentNo());
        }
        if (query.getPaymentAmountMin() != null) {
            queryWrapper.ge("payment_amount", query.getPaymentAmountMin());
        }
        if (query.getPaymentAmountMax() != null) {
            queryWrapper.le("payment_amount", query.getPaymentAmountMax());
        }
        if (query.getReceivedAmountMin() != null) {
            queryWrapper.ge("received_amount", query.getReceivedAmountMin());
        }
        if (query.getReceivedAmountMax() != null) {
            queryWrapper.le("received_amount", query.getReceivedAmountMax());
        }
        if (query.getCreateTimeStart() != null) {
            queryWrapper.ge("create_time", query.getCreateTimeStart());
        }
        if (query.getCreateTimeEnd() != null) {
            queryWrapper.le("create_time", query.getCreateTimeEnd());
        }
        
        // 构建分页参数
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ForeignExchangePayment> pageObj = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(query.getPage(), query.getSize());
        
        // 执行查询
        IPage<ForeignExchangePayment> resultPage = foreignExchangePaymentRepository.selectPage(pageObj, queryWrapper);
        
        // 转换为通用分页对象
        Page<ForeignExchangePayment> commonPage = new Page<>();
        commonPage.setRecords(resultPage.getRecords());
        commonPage.setTotal(resultPage.getTotal());
        commonPage.setPages(resultPage.getPages());
        commonPage.setSize(resultPage.getSize());
        commonPage.setCurrent(resultPage.getCurrent());
        
        return commonPage;
    }
}