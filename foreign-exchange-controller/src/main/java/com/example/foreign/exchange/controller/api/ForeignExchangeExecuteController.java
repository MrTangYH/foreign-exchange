package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteQueryRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteResponseVO;
import com.example.foreign.exchange.application.service.ForeignExchangeExecuteApplicationService;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.utils.EasyExcelUtil;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteQueryRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteRequestDTO;
import com.example.foreign.exchange.controller.converter.ForeignExchangeExecuteConverter;
import com.example.foreign.exchange.controller.dto.ForeignExchangeExecuteResponseDTO;
import com.example.foreign.exchange.controller.vo.ForeignExchangeExecuteExcelVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * 导出执行单列表为Excel
     */
    @PostMapping("/export")
    public void export(@RequestBody ForeignExchangeExecuteQueryRequestDTO dto, HttpServletResponse response) {
        try {
            // 转换DTO为VO
            ForeignExchangeExecuteQueryRequestVO vo = ForeignExchangeExecuteConverter.foreignExchangeExecuteQueryRequestDTO2VO(dto);
            // 设置分页参数，查询所有数据（上限10万条）
            vo.setPage(1L);
            vo.setSize(100000L);
            // 调用应用服务查询执行单列表
            Page<ForeignExchangeExecuteResponseVO> result = foreignExchangeExecuteApplicationService.queryExecuteOrderList(vo);
            
            // 转换为Excel VO列表
            List<ForeignExchangeExecuteExcelVO> excelVOList = new ArrayList<>();
            for (ForeignExchangeExecuteResponseVO execute : result.getRecords()) {
                ForeignExchangeExecuteExcelVO excelVO = new ForeignExchangeExecuteExcelVO();
                excelVO.setExecuteNo(execute.getOrderNo());
                excelVO.setApplyNo(execute.getApplyNo());
                excelVO.setDirection(execute.getDirection() == 1 ? "购汇" : "结汇");
                excelVO.setCurrency(execute.getCurrency());
                excelVO.setExecuteAmount(execute.getAmount());
                excelVO.setExecuteRate(execute.getRate());
                excelVO.setExecuteTime(execute.getCreateTime());
                excelVO.setStatus(getStatusText(execute.getStatus()));
                excelVO.setCreateTime(execute.getCreateTime());
                excelVOList.add(excelVO);
            }
            
            // 导出Excel
            EasyExcelUtil.exportExcel(response, excelVOList, "外汇交易执行列表", ForeignExchangeExecuteExcelVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\": 500, \"message\": \"导出数据失败：\" + e.getMessage()}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        switch (status) {
            case 1:
                return "执行单已生成";
            case 2:
                return "执行单已生成付款单";
            case 3:
                return "执行单已取消";
            case 4:
                return "执行单已作废";
            default:
                return "未知";
        }
    }
}