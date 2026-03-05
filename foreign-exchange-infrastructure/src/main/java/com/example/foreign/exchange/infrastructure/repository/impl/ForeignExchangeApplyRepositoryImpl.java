package com.example.foreign.exchange.infrastructure.repository.impl;

import com.example.foreign.exchange.domain.repository.ForeignExchangeApplyRepository;
import org.springframework.stereotype.Repository;

/**
 * 外汇申请仓储实现
 */
@Repository
public interface ForeignExchangeApplyRepositoryImpl extends ForeignExchangeApplyRepository {
    // 使用MyBatis-Plus的BaseMapper，无需写SQL
}