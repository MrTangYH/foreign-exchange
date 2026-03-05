package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.converter.ForeignExchangePaymentConverter;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentResponseVO;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;
import com.example.foreign.exchange.domain.service.ForeignExchangePaymentDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外汇付款应用服务
 */
@Service
public class ForeignExchangePaymentApplicationService {
    
    @Resource
    private RedisIdGenerator redisIdGenerator;
    
    @Resource
    private ForeignExchangePaymentDomainService foreignExchangePaymentDomainService;
    
    /**
     * 生成付款单
     * @param request 付款单请求VO
     * @return 生成结果
     */
    public String submitPaymentOrder(ForeignExchangePaymentRequestVO request) {
        // 生成付款单号
        String paymentNo = redisIdGenerator.generatePaymentNo();
        
        // 创建付款单实体
        ForeignExchangePayment payment = new ForeignExchangePayment();
        payment.setPaymentNo(paymentNo);
        payment.setOrderNo(request.getOrderNo());
        payment.setPaymentAmount(request.getPaymentAmount());
        payment.setPayCurrency(request.getPayCurrency());
        payment.setReceivedAmount(request.getReceivedAmount());
        payment.setReceivedCurrency(request.getReceivedCurrency());
        payment.setSubjectAccountNo(request.getSubjectAccountNo());
        payment.setCounterpartyAccountNo(request.getCounterpartyAccountNo());
        payment.setSwiftBic(request.getSwiftBic());
        payment.setStatus(request.getStatus());
        payment.setPaymentTime(request.getPaymentTime());
        payment.setFailureReason(request.getFailureReason());
        payment.setUserId(request.getUserId());
        payment.setCreateTime(request.getCreateTime());
        
        // 调用领域服务保存付款单
        foreignExchangePaymentDomainService.savePaymentOrder(payment);
        
        return "生成付款单成功";
    }
    
    /**
     * 查询付款单列表
     * @param request 查询请求VO
     * @return 付款单列表
     */
    public Page<ForeignExchangePaymentResponseVO> queryPaymentOrderList(ForeignExchangePaymentQueryRequestVO request) {
        // 转换VO为查询对象
        ForeignExchangePaymentQuery query = ForeignExchangePaymentConverter.foreignExchangePaymentQueryRequestVOToQuery(request);
        
        // 调用领域服务查询付款单列表
        Page<ForeignExchangePayment> paymentPage = foreignExchangePaymentDomainService.queryPaymentOrderList(query);
        
        // 转换为响应VO
        List<ForeignExchangePaymentResponseVO> responseVOs = paymentPage.getRecords().stream()
                .map(this::convertToResponseVO)
                .collect(Collectors.toList());
        
        // 构建响应分页对象
        Page<ForeignExchangePaymentResponseVO> responsePage = new Page<>();
        responsePage.setRecords(responseVOs);
        responsePage.setTotal(paymentPage.getTotal());
        responsePage.setPages(paymentPage.getPages());
        responsePage.setSize(paymentPage.getSize());
        responsePage.setCurrent(paymentPage.getCurrent());
        
        return responsePage;
    }
    
    /**
     * 将ForeignExchangePayment转换为ForeignExchangePaymentResponseVO
     */
    private ForeignExchangePaymentResponseVO convertToResponseVO(ForeignExchangePayment payment) {
        ForeignExchangePaymentResponseVO vo = new ForeignExchangePaymentResponseVO();
        vo.setId(payment.getId());
        vo.setPaymentNo(payment.getPaymentNo());
        vo.setOrderNo(payment.getOrderNo());
        vo.setPaymentAmount(payment.getPaymentAmount());
        vo.setPayCurrency(payment.getPayCurrency());
        vo.setReceivedAmount(payment.getReceivedAmount());
        vo.setReceivedCurrency(payment.getReceivedCurrency());
        vo.setSubjectAccountNo(payment.getSubjectAccountNo());
        vo.setCounterpartyAccountNo(payment.getCounterpartyAccountNo());
        vo.setSwiftBic(payment.getSwiftBic());
        vo.setStatus(payment.getStatus());
        vo.setPaymentTime(payment.getPaymentTime());
        vo.setFailureReason(payment.getFailureReason());
        vo.setUserId(payment.getUserId());
        vo.setCreateTime(payment.getCreateTime());
        return vo;
    }
}