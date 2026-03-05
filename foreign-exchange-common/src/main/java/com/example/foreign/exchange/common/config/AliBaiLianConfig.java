package com.example.foreign.exchange.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 阿里百炼配置类
 */
@Configuration
public class AliBaiLianConfig {
    @Value("${aliyun.bailian.api-key}")
    private String apiKey;

    @Value("${aliyun.bailian.default-model:qwen-turbo}")
    private String defaultModel;

    // 全局初始化
    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("阿里百炼 API Key 未配置！");
        }
        // 设置环境变量
        System.setProperty("DASHSCOPE_API_KEY", apiKey);
    }

    // getter
    public String getApiKey() {
        return apiKey;
    }
    
    public String getDefaultModel() {
        return defaultModel;
    }
}