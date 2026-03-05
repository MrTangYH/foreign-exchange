package com.example.foreign.exchange.application.service.impl;

import com.example.foreign.exchange.application.service.ExchangeRateService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇率服务实现类
 */
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    // 外部汇率接口URL
    private static final String EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/40f38cc1afa36d1dabf0bfe0/latest/CNY";

    // Redis键格式：EXCHANGE_RATE_yyyy-MM-dd_币种
    private static final String REDIS_KEY_PREFIX = "EXCHANGE_RATE_";

    // 需要获取的币种列表
    private static final List<String> TARGET_CURRENCIES = List.of("USD", "JPY", "EUR", "HKD");

    /**
     * 获取实时汇率
     * 每3小时调用外部接口获取汇率，并保存到Redis
     * @return 汇率数据列表
     */
    @Override
    public List<Map<String, Object>> getRealTimeExchangeRate() {
        // 调用外部接口获取汇率
        List<Map<String, Object>> exchangeRates = fetchExchangeRatesFromApi();
        
        // 保存到Redis
        saveExchangeRatesToRedis(exchangeRates);
        
        return exchangeRates;
    }

    /**
     * 从外部API获取汇率数据
     * @return 汇率数据列表
     */
    private List<Map<String, Object>> fetchExchangeRatesFromApi() {
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        // 构建请求实体
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // 调用外部API
        ResponseEntity<Map> response = restTemplate.exchange(
                EXCHANGE_RATE_API_URL,
                HttpMethod.GET,
                entity,
                Map.class
        );
        
        // 处理响应数据
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> exchangeRates = new ArrayList<>();
        
        // 处理外部API返回的数据结构
        if (responseBody != null && "success".equals(responseBody.get("result"))) {
            Map<String, Object> conversionRates = (Map<String, Object>) responseBody.get("conversion_rates");
            if (conversionRates != null) {
                LocalDateTime now = LocalDateTime.now();
                String timestamp = now.toString();
                
                // 只处理指定的币种
                for (String currency : TARGET_CURRENCIES) {
                    if (conversionRates.containsKey(currency)) {
                        Map<String, Object> exchangeRate = new HashMap<>();
                        exchangeRate.put("currency", currency);
                        exchangeRate.put("rate", conversionRates.get(currency));
                        exchangeRate.put("timestamp", timestamp);
                        exchangeRates.add(exchangeRate);
                    }
                }
            }
        }
        
        return exchangeRates;
    }

    /**
     * 将汇率数据保存到Redis
     * @param exchangeRates 汇率数据列表
     */
    private void saveExchangeRatesToRedis(List<Map<String, Object>> exchangeRates) {
        // 获取当天日期
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 为每个币种保存汇率数据
        for (Map<String, Object> rate : exchangeRates) {
            String currency = (String) rate.get("currency");
            String redisKey = REDIS_KEY_PREFIX + dateStr + "_" + currency;

            // 从Redis获取现有数据
            List<Double> existingRates = (List<Double>) redisTemplate.opsForValue().get(redisKey);
            if (existingRates == null) {
                existingRates = new ArrayList<>();
            }

            // 添加新的汇率数据（只保存汇率值）
            Double rateValue = Double.parseDouble(rate.get("rate").toString());
            existingRates.add(rateValue);

            // 保存回Redis
            redisTemplate.opsForValue().set(redisKey, existingRates);
        }
    }

    /**
     * 定时任务，每3小时调用一次外部接口获取汇率
     */
    @Scheduled(cron = "0 0 */3 * * ?") // 每3小时执行一次
    public void scheduledFetchExchangeRates() {
        try {
            getRealTimeExchangeRate();
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
        }
    }
}