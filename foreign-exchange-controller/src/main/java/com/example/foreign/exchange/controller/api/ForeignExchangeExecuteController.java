package com.example.foreign.exchange.controller.api;

import com.alibaba.excel.util.StringUtils;
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
import com.example.foreign.exchange.application.entity.ForeignExchangeExecuteExcelVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.StringUtil;
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
            if (StringUtils.isEmpty(result)){
                return ApiResponseDTO.fail(400, "生成执行单失败");
            }
            // todo:硬编码判断交易执行单是否生成成功，后续修改
            if (result.charAt(0) == '交'){
                return ApiResponseDTO.fail(400, "请勿重复创建交易执行单");
            }
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
            
            // 转换为Excel VO列表
            List<ForeignExchangeExecuteExcelVO> excelVOList = foreignExchangeExecuteApplicationService.exportApplyList(vo);
            
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

}