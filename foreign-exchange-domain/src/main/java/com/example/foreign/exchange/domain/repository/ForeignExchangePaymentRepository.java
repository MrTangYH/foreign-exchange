package com.example.foreign.exchange.domain.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.ForeignExchangePayment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外汇付款Repository
 */
@Mapper
public interface ForeignExchangePaymentRepository extends BaseMapper<ForeignExchangePayment> {
}