package com.example.foreign.exchange.domain.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.domain.entity.ForeignExchangeAccount;
import com.example.foreign.exchange.domain.entity.ForeignExchangeAccountTransaction;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.enums.PaymentStatusResultEnum;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;
import com.example.foreign.exchange.domain.repository.ForeignExchangeAccountRepository;
import com.example.foreign.exchange.domain.repository.ForeignExchangeAccountTransactionRepository;
import com.example.foreign.exchange.domain.repository.ForeignExchangePaymentRepository;
import com.example.foreign.exchange.domain.service.ForeignExchangePaymentDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 外汇付款领域服务实现
 */
@Service("ForeignExchangePaymentDomainService")
public class ForeignExchangePaymentDomainServiceImpl implements ForeignExchangePaymentDomainService {
    
    @Resource
    private ForeignExchangePaymentRepository foreignExchangePaymentRepository;
    
    @Resource
    private ForeignExchangeAccountRepository foreignExchangeAccountRepository;
    
    @Resource
    private ForeignExchangeAccountTransactionRepository foreignExchangeAccountTransactionRepository;
    
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
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ForeignExchangePayment> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(query.getPage(), query.getSize());
        
        // 执行查询
        IPage<ForeignExchangePayment> resultPage = foreignExchangePaymentRepository.selectPage(page, queryWrapper);
        
        // 转换为通用分页对象
        Page<ForeignExchangePayment> commonPage = new Page<>();
        commonPage.setRecords(resultPage.getRecords());
        commonPage.setTotal(resultPage.getTotal());
        commonPage.setPages(resultPage.getCurrent());
        commonPage.setSize(resultPage.getSize());
        
        return commonPage;
    }
    
    @Override
    public boolean updatePaymentStatus(String paymentNo, Integer status, String failureReason) {
        // 构建查询条件
        QueryWrapper<ForeignExchangePayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_no", paymentNo);
        
        // 构建更新对象
        ForeignExchangePayment payment = new ForeignExchangePayment();
        payment.setStatus(status);
        if (!status.equals(PaymentStatusResultEnum.PAYMENT_SUCCESS.getCode())) {
            payment.setFailureReason(failureReason);
        }
        if (status.equals(PaymentStatusResultEnum.PAYMENT_SUCCESS.getCode())) {
            payment.setPaymentTime(LocalDateTime.now());
        }
        
        // 执行更新
        int result = foreignExchangePaymentRepository.update(payment, queryWrapper);
        
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentStatusResultEnum handlePaymentStatusChange(String paymentNo, String subjectAccountNo, String payCurrency, BigDecimal paymentAmount) {
        // 1. 查询账户信息
        QueryWrapper<ForeignExchangeAccount> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("account_no", subjectAccountNo);
        accountQueryWrapper.eq("currency", payCurrency);
        ForeignExchangeAccount account = foreignExchangeAccountRepository.selectOne(accountQueryWrapper);
        
        // 2. 检查账户是否存在
        if (account == null) {
            // 账号或币种错误，更新付款单状态为失败
            updatePaymentStatus(paymentNo, PaymentStatusResultEnum.ACCOUNT_OR_CURRENCY_ERROR.getCode(),
                    PaymentStatusResultEnum.ACCOUNT_OR_CURRENCY_ERROR.getMessage());
            return PaymentStatusResultEnum.ACCOUNT_OR_CURRENCY_ERROR;
        }
        
        // 3. 检查余额是否充足
        if (account.getBalance().compareTo(paymentAmount) < 0) {
            // 余额不足，更新付款单状态为失败
            updatePaymentStatus(paymentNo, PaymentStatusResultEnum.INSUFFICIENT_BALANCE.getCode(),
                    PaymentStatusResultEnum.INSUFFICIENT_BALANCE.getMessage());
            return PaymentStatusResultEnum.INSUFFICIENT_BALANCE;
        }
        
        // 4. 余额充足，执行扣款操作
        // 4.1 计算扣款后的余额
        BigDecimal newBalance = account.getBalance().subtract(paymentAmount);
        
        // 4.2 更新账户余额
        account.setBalance(newBalance);
        account.setUpdateTime(LocalDateTime.now());
        foreignExchangeAccountRepository.updateById(account);
        
        // 4.3 记录交易流水
        ForeignExchangeAccountTransaction transaction = new ForeignExchangeAccountTransaction();
        transaction.setAccountNo(subjectAccountNo);
        transaction.setPaymentNo(paymentNo);
        transaction.setType("DEDUCT");
        transaction.setAmount(paymentAmount);
        transaction.setBalanceAfter(newBalance);
        transaction.setTransactionTime(LocalDateTime.now());
        foreignExchangeAccountTransactionRepository.insert(transaction);
        
        // 4.4 更新付款单状态为成功
        updatePaymentStatus(paymentNo, PaymentStatusResultEnum.PAYMENT_SUCCESS.getCode(),
                PaymentStatusResultEnum.PAYMENT_SUCCESS.getMessage());
        
        return PaymentStatusResultEnum.PAYMENT_SUCCESS;
    }
}