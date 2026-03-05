package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.service.ExchangeRateService;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 汇率相关接口
 */
@RestController
@RequestMapping("/rate")
public class ForeignExchangeRateController {

    @Resource
    private ExchangeRateService exchangeRateService;

    /**
     * 实时汇率接口
     * 每3小时调用外部接口获取汇率，并保存到Redis
     * 返回当前汇率数据给前端
     */
    @GetMapping("/real_time_exchange_rate")
    public ApiResponseDTO getRealTimeExchangeRate() {
        try {
            // 调用服务获取实时汇率
            List<Map<String, Object>> exchangeRates = exchangeRateService.getRealTimeExchangeRate();
            // 返回结果
            return ApiResponseDTO.success("获取实时汇率成功", exchangeRates);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "获取实时汇率失败：" + e.getMessage());
        }
    }
}