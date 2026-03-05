package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.application.service.ForeignExchangeExecuteApplicationService;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteRequestDTO;
import com.example.foreign.exchange.controller.converter.ForeignExchangeExecuteConverter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外汇执行单控制器
 */
@RestController
@RequestMapping("/execute")
public class ForeignExchangeExecuteController {
    
    @Resource
    private ForeignExchangeExecuteApplicationService foreignExchangeExecuteApplicationService;
    
    /**
     * 生成执行单
     */
    @PostMapping("/submit")
    public ApiResponseDTO submitExecuteOrder(@RequestBody ForeignExchangeExecuteRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangeExecuteRequestVO vo = ForeignExchangeExecuteConverter.foreignExchangeExecuteRequestDTO2VO(dto);
            // 调用应用服务生成执行单
            String result = foreignExchangeExecuteApplicationService.submitExecuteOrder(vo);
            // 返回结果
            return ApiResponseDTO.success("交易执行单生成成功", result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "生成执行单失败：" + e.getMessage());
        }
    }
}