package com.example.foreign.exchange.domain.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foreign.exchange.domain.entity.ForeignExchangeApply;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外汇申请仓储接口
 */
@Mapper
public interface ForeignExchangeApplyRepository extends BaseMapper<ForeignExchangeApply> {
    // 继承MyBatis-Plus的BaseMapper，获得CRUD方法
}