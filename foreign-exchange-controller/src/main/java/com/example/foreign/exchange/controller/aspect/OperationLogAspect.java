package com.example.foreign.exchange.controller.aspect;

import com.example.foreign.exchange.domain.entity.OperationLog;
import com.example.foreign.exchange.domain.service.OperationLogDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {
    
    @Resource
    private OperationLogDomainService operationLogDomainService;
    
    @Resource
    private ObjectMapper objectMapper;
    
    /**
     * 定义切点：拦截所有Controller中的方法，排除query相关的方法和实时汇率查询接口
     */
    @Pointcut("execution(* com.example.foreign.exchange.controller.api.*Controller.*(..)) && !execution(* com.example.foreign.exchange.controller.api.*Controller.*query*(..)) && !execution(* com.example.foreign.exchange.controller.api.*Controller.*rate*(..))")
    public void operationLogPointcut() {
    }
    
    /**
     * 环绕通知：记录操作日志
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 构建操作日志
        OperationLog log = new OperationLog();
        
        // 获取操作人ID（这里假设从请求中获取，实际项目中可能从token或session中获取）
        // 临时设置为1，实际项目中需要根据具体情况修改
        log.setUserId(1L);
        
        // 获取客户端IP
        String ipAddress = getClientIp(request);
        log.setIpAddress(ipAddress);
        
        // 获取调用的API接口名称
        String api = request.getRequestURI();
        log.setApi(api);
        
        // 获取所属模块
        String module = getModuleFromApi(api);
        log.setModule(module);
        
        // 获取请求详情
        try {
            // 获取方法参数
            Object[] args = joinPoint.getArgs();
            // 转换为JSON字符串
            String requestJson = objectMapper.writeValueAsString(args);
            log.setRequest(requestJson);
        } catch (Exception e) {
            log.setRequest("无法序列化请求参数");
        }
        
        // 执行目标方法
        Object result = joinPoint.proceed();
        
        // 获取响应详情
        try {
            // 转换为JSON字符串
            String responseJson = objectMapper.writeValueAsString(result);
            log.setResponse(responseJson);
        } catch (Exception e) {
            log.setResponse("无法序列化响应结果");
        }
        
        // 设置操作时间
        log.setCreateTime(LocalDateTime.now());
        
        // 保存操作日志
        operationLogDomainService.saveOperationLog(log);
        
        return result;
    }
    
    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 从API路径中获取模块名称
     */
    private String getModuleFromApi(String api) {
        // 根据API路径解析模块名称
        if (api.contains("/apply")) {
            return "外汇申请";
        } else if (api.contains("/execute")) {
            return "资金执行";
        } else if (api.contains("/pay")) {
            return "付款管理";
        } else {
            return "其他模块";
        }
    }
}