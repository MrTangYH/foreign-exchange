package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import com.example.foreign.exchange.domain.enums.ExecuteStatusEnum;
import com.example.foreign.exchange.domain.service.ForeignExchangeExecuteDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 外汇执行单应用服务
 */
@Service
public class ForeignExchangeExecuteApplicationService {
    
    @Resource
    private RedisIdGenerator redisIdGenerator;
    
    @Resource
    private ForeignExchangeExecuteDomainService foreignExchangeExecuteDomainService;
    
    /**
     * 生成执行单
     * @param request 执行单请求VO
     * @return 生成结果
     */
    public String submitExecuteOrder(ForeignExchangeExecuteRequestVO request) {
        // 生成执行单号
        String orderNo = redisIdGenerator.generateOrderNo();
        
        // 创建执行单实体
        ForeignExchangeOrder order = new ForeignExchangeOrder();
        order.setOrderNo(orderNo);
        order.setApplyNo(request.getApplyNo());
        order.setCurrency(request.getCurrency());
        order.setAmount(request.getAmount());
        order.setRmbAmount(request.getRmbAmount());
        order.setRate(request.getRate());
        order.setTransactionSubject(request.getTransactionSubject());
        order.setSubjectAccountNo(request.getSubjectAccountNo());
        order.setCounterparty(request.getCounterparty());
        order.setCounterpartyAccountNo(request.getCounterpartyAccountNo());
        order.setSwiftBic(request.getSwiftBic());
        order.setStatus(ExecuteStatusEnum.GENERATED.getCode());
        order.setUserId(request.getUserId());
        
        // 调用领域服务保存执行单
        foreignExchangeExecuteDomainService.saveExecuteOrder(order);
        
        return "生成付款单成功";
    }
}