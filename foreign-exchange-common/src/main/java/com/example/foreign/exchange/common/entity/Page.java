package com.example.foreign.exchange.common.entity;

import lombok.Data;

import java.util.List;

/**
 * 分页信息类
 */
@Data
public class Page<T> {
    /**
     * 当前页码
     */
    private Long current;
    
    /**
     * 每页大小
     */
    private Long size;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 数据列表
     */
    private List<T> records;
}