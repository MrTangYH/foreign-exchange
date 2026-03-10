package com.example.foreign.exchange.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Data
public class OpenWebUiConfig {
    @Value("${open.web.ui.api-key}")
    private String apiKey;

    @Value("${open.web.ui.default-model}")
    private String defaultModel;

    @Value("${open.web.ui.url}")
    private String url;

}
