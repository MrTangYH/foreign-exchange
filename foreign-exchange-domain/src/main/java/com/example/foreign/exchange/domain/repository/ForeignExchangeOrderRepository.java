package com.example.foreign.exchange.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.ForeignExchangeOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外汇执行单Repository
 */
@Mapper
public interface ForeignExchangeOrderRepository extends BaseMapper<ForeignExchangeOrder> {
}