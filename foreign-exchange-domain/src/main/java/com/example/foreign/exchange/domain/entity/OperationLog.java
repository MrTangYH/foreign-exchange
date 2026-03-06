package com.example.foreign.exchange.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
@TableName("operation_log")
public class OperationLog {
    /**
     * 主键ID，自增，性能优于UUID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 操作人ID（外键关联用户表），即使用户被删除仍可追溯身份
     */
    private Long userId;
    
    /**
     * 客户端IP（支持IPv6），用于定位操作来源
     */
    private String ipAddress;
    
    /**
     * 调用的API接口名称（如 "/api/fx/apply"），便于快速定位业务逻辑
     */
    private String api;
    
    /**
     * 所属模块（如“外汇申请”、“资金执行”），支持按业务域筛选
     */
    private String module;
    
    /**
     * 请求详情快照，以JSON格式存储方法参数），避免为每种操作设计独立表结构
     */
    private String request;
    
    /**
     * 响应详情快照，以JSON格式存储方法参，避免为每种操作设计独立表结构
     */
    private String response;
    
    /**
     * 操作时间戳，精度到毫秒，满足《网络安全法》对操作时间精确记录的要求
     */
    private LocalDateTime createTime;
}