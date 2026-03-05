package com.example.foreign.exchange.application.service;

import java.util.List;
import java.util.Map;

/**
 * 汇率服务接口
 */
public interface ExchangeRateService {
    
    /**
     * 获取实时汇率
     * 每3小时调用外部接口获取汇率，并保存到Redis
     * @return 汇率数据列表
     */
    List<Map<String, Object>> getRealTimeExchangeRate();
}