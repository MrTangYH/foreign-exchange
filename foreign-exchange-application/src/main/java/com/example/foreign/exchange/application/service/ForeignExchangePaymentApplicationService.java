package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.converter.ForeignExchangePaymentConverter;
import com.example.foreign.exchange.application.entity.*;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import com.example.foreign.exchange.domain.enums.ExecuteStatusEnum;
import com.example.foreign.exchange.domain.enums.PaymentStatusResultEnum;
import com.example.foreign.exchange.domain.query.ForeignExchangePaymentQuery;
import com.example.foreign.exchange.domain.service.ForeignExchangeExecuteDomainService;
import com.example.foreign.exchange.domain.service.ForeignExchangePaymentDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外汇付款应用服务
 */
@Service
public class ForeignExchangePaymentApplicationService {

    private static final Long excelQueryPage = 1L;
    private static final Long excelQuerySize = 100000L;
    
    @Resource
    private RedisIdGenerator redisIdGenerator;
    
    @Resource
    private ForeignExchangePaymentDomainService foreignExchangePaymentDomainService;
    
    @Resource
    private ForeignExchangeExecuteDomainService foreignExchangeExecuteDomainService;
    
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
        payment.setStatus(PaymentStatusResultEnum.PENDING_PAYMENT.getCode());
        payment.setPaymentTime(request.getPaymentTime());
        payment.setFailureReason(request.getFailureReason());
        payment.setUserId(request.getUserId());
        payment.setCreateTime(request.getCreateTime());
        
        // 调用领域服务保存付款单
        foreignExchangePaymentDomainService.savePaymentOrder(payment);
        
        // 更新执行单状态为付款单已生成（状态码2）
        foreignExchangeExecuteDomainService.updateOrderStatus(request.getOrderNo(), ExecuteStatusEnum.PAYMENT_GENERATED.getCode());
        
        return paymentNo;
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
     * 处理支付状态变更
     * @param request 支付状态变更请求VO
     * @return 支付状态结果
     */
    public PaymentStatusResultEnum handlePaymentStatusChange(ForeignExchangePaymentStatusRequestVO request) {
        // 调用领域服务处理支付状态变更
        PaymentStatusResultEnum result = foreignExchangePaymentDomainService.handlePaymentStatusChange(
                request.getPaymentNo(),
                request.getSubjectAccountNo(),
                request.getPayCurrency(),
                request.getPaymentAmount()
        );
        
        return result;
    }

    /**
     * 取消付款单
     * @param request 取消付款单请求VO
     * @return 付款单号
     */
    public String cancelPayment(ForeignExchangePaymentStatusRequestVO request) {
        // 调用领域服务取消付款单
        return foreignExchangePaymentDomainService.cancelPayment(request.getPaymentNo());
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

    /**
     * 导出外汇申请列表
     */
    public List<ForeignExchangePaymentExcelVO> exportApplyList(ForeignExchangePaymentQueryRequestVO request) {
        request.setPage(excelQueryPage);
        request.setSize(excelQuerySize);
        Page<ForeignExchangePaymentResponseVO> executeList = queryPaymentOrderList(request);
        List<ForeignExchangePaymentExcelVO> excelVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(executeList.getRecords())) {
            return new ArrayList<>();
        }
        excelVOList = executeList.getRecords()
                // 1. 将列表转为Stream流
                .stream()
                // 2. 对流中的每个元素做转换（等同于循环里的Converter调用）
                .map(ForeignExchangePaymentConverter::foreignExchangePaymentResponseVOToExcelVO)
                // 3. 将转换后的流收集成List（指定ArrayList，和原逻辑保持一致）
                .collect(Collectors.toList());
        return excelVOList;
    }
}