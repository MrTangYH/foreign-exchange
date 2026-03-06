package com.example.foreign.exchange.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Repository
 */
@Mapper
public interface OperationLogRepository extends BaseMapper<OperationLog> {
}