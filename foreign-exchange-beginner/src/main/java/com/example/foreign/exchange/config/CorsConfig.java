package com.example.foreign.exchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局跨域配置：允许前端跨域访问后端接口
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 1. 允许前端域名访问（开发环境允许所有域名，生产环境替换为具体域名）
//        config.addAllowedOrigin("http://localhost:5173"); // 精准允许Vite前端地址
//        config.addAllowedOrigin("*"); // 测试环境可直接允许所有（不推荐生产）
        config.addAllowedOriginPattern("*");
        // 2. 允许所有请求方法（GET/POST/PUT/DELETE等）
        config.addAllowedMethod("*");

        // 3. 允许所有请求头（比如Token、Content-Type等）
        config.addAllowedHeader("*");

        // 4. 允许携带Cookie（如需）
        config.setAllowCredentials(true);

        // 5. 预检请求有效期（秒），避免频繁发OPTIONS请求
        config.setMaxAge(3600L);

        // 配置跨域规则生效的接口路径（所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}