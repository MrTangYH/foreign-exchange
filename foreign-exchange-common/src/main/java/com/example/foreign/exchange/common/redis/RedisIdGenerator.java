package com.example.foreign.exchange.common.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Redis ID生成器
 */
@Component
public class RedisIdGenerator {
    @Resource
    private RedisTemplate<String, Long> redisTemplate;
    
    private static final String APPLY_KEY_PREFIX = "foreign_exchange:apply_no:";
    private static final String EXECUTE_KEY_PREFIX = "foreign_exchange:order_no:";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /**
     * 生成外汇申请编号
     * 格式：FXAPPLY+YYYYMMDD+6位序列
     * @return 申请编号
     */
    public String generateApplyNo() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        String key = APPLY_KEY_PREFIX + dateStr;
        
        // 使用Redis自增生成6位序列
        Long sequence = redisTemplate.opsForValue().increment(key);
        
        // 设置过期时间为2天，确保键不会永久存在
        redisTemplate.expire(key, 2, java.util.concurrent.TimeUnit.DAYS);
        
        // 格式化为6位，不足前面补0
        String sequenceStr = String.format("%06d", sequence);
        
        return "FXAPPLY" + dateStr + sequenceStr;
    }

    /**
     * 生成外汇交易执行编号
     * 格式：FXAPPLY+YYYYMMDD+6位序列
     * @return 申请编号
     */
    public String generateOrderNo() {
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        String key = EXECUTE_KEY_PREFIX + dateStr;

        // 使用Redis自增生成6位序列
        Long sequence = redisTemplate.opsForValue().increment(key);

        // 设置过期时间为2天，确保键不会永久存在
        redisTemplate.expire(key, 2, java.util.concurrent.TimeUnit.DAYS);

        // 格式化为6位，不足前面补0
        String sequenceStr = String.format("%06d", sequence);

        return "FXORDER" + dateStr + sequenceStr;
    }
}