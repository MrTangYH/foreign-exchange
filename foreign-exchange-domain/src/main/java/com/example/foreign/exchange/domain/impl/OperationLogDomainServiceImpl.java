package com.example.foreign.exchange.domain.impl;

import com.example.foreign.exchange.domain.entity.OperationLog;
import com.example.foreign.exchange.domain.repository.OperationLogRepository;
import com.example.foreign.exchange.domain.service.OperationLogDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 操作日志领域服务实现
 */
@Service("OperationLogDomainService")
public class OperationLogDomainServiceImpl implements OperationLogDomainService {
    
    @Resource
    private OperationLogRepository operationLogRepository;
    
    @Override
    public Long saveOperationLog(OperationLog log) {
        // 设置创建时间
        if (log.getCreateTime() == null) {
            log.setCreateTime(LocalDateTime.now());
        }
        
        // 保存到数据库
        operationLogRepository.insert(log);
        
        return log.getId();
    }
}