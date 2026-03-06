package com.example.foreign.exchange.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.ForeignExchangeAccountTransaction;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外汇账户交易记录Repository
 */
@Mapper
public interface ForeignExchangeAccountTransactionRepository extends BaseMapper<ForeignExchangeAccountTransaction> {
}