package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.converter.ForeignExchangeExecuteConverter;
import com.example.foreign.exchange.application.entity.*;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.redis.RedisIdGenerator;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import com.example.foreign.exchange.domain.enums.ExecuteStatusEnum;
import com.example.foreign.exchange.domain.service.ForeignExchangeExecuteDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外汇执行单应用服务
 */
@Service
public class ForeignExchangeExecuteApplicationService {

    private static final Long excelQueryPage = 1L;
    private static final Long excelQuerySize = 100000L;
    
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
        order.setDirection(request.getDirection());
        
        // 调用领域服务保存执行单
        return foreignExchangeExecuteDomainService.saveExecuteOrder(order);
    }

    /**
     * 查询执行单列表
     * @param request 查询请求VO
     * @return 执行单列表
     */
    public Page<ForeignExchangeExecuteResponseVO> queryExecuteOrderList(ForeignExchangeExecuteQueryRequestVO request) {
        ForeignExchangeOrder executeOrder = ForeignExchangeExecuteConverter.foreignExchangeExecuteQueryRequestVOToPO(request);
        // 调用领域服务查询执行单列表
        Page<ForeignExchangeOrder> orderPage = foreignExchangeExecuteDomainService.queryExecuteOrderList(executeOrder, request.getPage(), request.getSize());

        // 转换为响应VO
        List<ForeignExchangeExecuteResponseVO> responseVOs = orderPage.getRecords().stream()
                .map(this::convertToResponseVO)
                .collect(Collectors.toList());

        // 构建响应分页对象
        Page<ForeignExchangeExecuteResponseVO> responsePage = new Page<>();
        responsePage.setRecords(responseVOs);
        responsePage.setTotal(orderPage.getTotal());
        responsePage.setCurrent(orderPage.getCurrent());
        responsePage.setSize(orderPage.getSize());
        responsePage.setPages(orderPage.getPages());

        return responsePage;
    }

    /**
     * 将ForeignExchangeOrder转换为ForeignExchangeExecuteResponseVO
     */
    private ForeignExchangeExecuteResponseVO convertToResponseVO(ForeignExchangeOrder order) {
        ForeignExchangeExecuteResponseVO vo = new ForeignExchangeExecuteResponseVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setApplyNo(order.getApplyNo());
        vo.setDirection(order.getDirection());
        vo.setCurrency(order.getCurrency());
        vo.setAmount(order.getAmount());
        vo.setRmbAmount(order.getRmbAmount());
        vo.setRate(order.getRate());
        vo.setTransactionSubject(order.getTransactionSubject());
        vo.setSubjectAccountNo(order.getSubjectAccountNo());
        vo.setCounterparty(order.getCounterparty());
        vo.setCounterpartyAccountNo(order.getCounterpartyAccountNo());
        vo.setSwiftBic(order.getSwiftBic());
        vo.setStatus(order.getStatus());
        vo.setUserId(order.getUserId());
        vo.setCreateTime(order.getCreateTime());
        return vo;
    }

    /**
     * 导出外汇交易列表
     */
    public List<ForeignExchangeExecuteExcelVO> exportApplyList(ForeignExchangeExecuteQueryRequestVO request) {
        request.setPage(excelQueryPage);
        request.setSize(excelQuerySize);
        Page<ForeignExchangeExecuteResponseVO> executeList = queryExecuteOrderList(request);
        List<ForeignExchangeExecuteExcelVO> excelVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(executeList.getRecords())) {
            return new ArrayList<>();
        }
        excelVOList = executeList.getRecords()
                // 1. 将列表转为Stream流
                .stream()
                // 2. 对流中的每个元素做转换（等同于循环里的Converter调用）
                .map(ForeignExchangeExecuteConverter::foreignExchangeExecuteResponseVOToExcelVO)
                // 3. 将转换后的流收集成List（指定ArrayList，和原逻辑保持一致）
                .collect(Collectors.toList());
        return excelVOList;
    }
}