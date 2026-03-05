package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteResponseVO;
import com.example.foreign.exchange.application.service.ForeignExchangeExecuteApplicationService;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteQueryRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteRequestDTO;
import com.example.foreign.exchange.controller.converter.ForeignExchangeExecuteConverter;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteResponseDTO;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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

    /**
     * 查询执行单列表
     */
    @PostMapping("/query_list")
    public ApiResponseDTO queryExecuteOrderList(@RequestBody ForeignExchangeExecuteQueryRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangeExecuteQueryRequestVO vo = ForeignExchangeExecuteConverter.foreignExchangeExecuteQueryRequestDTO2VO(dto);
            // 调用应用服务查询执行单列表
            Page<ForeignExchangeExecuteResponseVO> result = foreignExchangeExecuteApplicationService.queryExecuteOrderList(vo);
            Page<ForeignExchangeExecuteResponseDTO> responsePage = ForeignExchangeExecuteConverter.foreignExchangeExecuteResponseVOPage2DTOPage(result);
            // 返回结果
            return ApiResponseDTO.success("查询成功", responsePage);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询执行单列表失败：" + e.getMessage());
        }
    }
    /**
     * 查询执行单列表
     */
    @PostMapping("/query_by_order_no")
    public ApiResponseDTO queryExecuteOrderByOrderNo(@RequestBody ForeignExchangeExecuteQueryRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangeExecuteQueryRequestVO vo = ForeignExchangeExecuteConverter.foreignExchangeExecuteQueryRequestDTO2VO(dto);
            // 调用应用服务查询执行单列表
            Page<ForeignExchangeExecuteResponseVO> result = foreignExchangeExecuteApplicationService.queryExecuteOrderList(vo);
            if (result != null && !CollectionUtils.isEmpty(result.getRecords())) {
                ForeignExchangeExecuteResponseDTO responsePage = ForeignExchangeExecuteConverter.foreignExchangeExecuteResponseVO2DTO(result.getRecords().get(0));
                return ApiResponseDTO.success("查询成功", responsePage);
            }
            // 返回结果
            return ApiResponseDTO.success("查询成功", new ArrayList<>());
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询执行单列表失败：" + e.getMessage());
        }
    }
}