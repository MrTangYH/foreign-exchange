package com.example.foreign.exchange.domain.service;

import com.example.foreign.exchange.domain.entity.OperationLog;

/**
 * 操作日志领域服务
 */
public interface OperationLogDomainService {
    /**
     * 保存操作日志
     * @param log 操作日志实体
     * @return 操作日志ID
     */
    Long saveOperationLog(OperationLog log);
}