package com.example.foreign.exchange.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.ForeignExchangeAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外汇账户Repository
 */
@Mapper
public interface ForeignExchangeAccountRepository extends BaseMapper<ForeignExchangeAccount> {
}