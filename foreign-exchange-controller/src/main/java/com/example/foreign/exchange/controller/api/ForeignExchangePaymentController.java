package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.entity.*;
import com.example.foreign.exchange.application.entity.ForeignExchangePaymentQueryRequestVO;
import com.example.foreign.exchange.application.service.ForeignExchangePaymentApplicationService;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.utils.EasyExcelUtil;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentQueryRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentRequestDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentStatusRequestDTO;
import com.example.foreign.exchange.controller.converter.ForeignExchangePaymentConverter;
import com.example.foreign.exchange.controller.dto.ForeignExchangePaymentResponseDTO;
import com.example.foreign.exchange.controller.vo.ForeignExchangePaymentExcelVO;
import com.example.foreign.exchange.domain.enums.PaymentStatusResultEnum;
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
 * 外汇付款控制器
 */
@RestController
@RequestMapping("/pay")
public class ForeignExchangePaymentController {
    
    @Resource
    private ForeignExchangePaymentApplicationService foreignExchangePaymentApplicationService;
    
    /**
     * 生成付款单
     */
    @PostMapping("/submit")
    public ApiResponseDTO submitPaymentOrder(@RequestBody ForeignExchangePaymentRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentRequestDTO2VO(dto);
            // 调用应用服务生成付款单
            String result = foreignExchangePaymentApplicationService.submitPaymentOrder(vo);
            // 返回结果
            return ApiResponseDTO.success("生成付款单成功", result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "生成付款单失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询付款单列表
     */
    @PostMapping("/query_list")
    public ApiResponseDTO queryPaymentOrderList(@RequestBody ForeignExchangePaymentQueryRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentQueryRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentQueryRequestDTO2VO(dto);
            // 调用应用服务查询付款单列表
            Page<ForeignExchangePaymentResponseVO> result = foreignExchangePaymentApplicationService.queryPaymentOrderList(vo);
            // 转换为响应DTO
            var responsePage = ForeignExchangePaymentConverter.foreignExchangePaymentResponseVOPage2DTOPage(result);
            // 返回结果
            return ApiResponseDTO.success("查询成功", responsePage);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询付款单列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 处理支付状态变更
     */
    @PostMapping("/change_status")
    public ApiResponseDTO changePaymentStatus(@RequestBody ForeignExchangePaymentStatusRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentStatusRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentStatusRequestDTO2VO(dto);
            // 调用应用服务处理支付状态变更
            PaymentStatusResultEnum result = foreignExchangePaymentApplicationService.handlePaymentStatusChange(vo);

            // 返回结果
            if (result.getCode() == PaymentStatusResultEnum.PAYMENT_SUCCESS.getCode()) {
                // 付款成功
                return ApiResponseDTO.success(PaymentStatusResultEnum.PAYMENT_SUCCESS.getMessage(), dto.getPaymentNo());
            } else {
                // 操作失败，返回失败响应
                return ApiResponseDTO.fail(400, result.getMessage());
            }
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "处理支付状态变更失败：" + e.getMessage());
        }
    }
    
    /**
     * 取消付款单
     */
    @PostMapping("/cancel")
    public ApiResponseDTO cancelPayment(@RequestBody ForeignExchangePaymentStatusRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentStatusRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentStatusRequestDTO2VO(dto);
            // 调用应用服务取消付款单
            String result = foreignExchangePaymentApplicationService.cancelPayment(vo);
            // 返回结果
            if (result.equals(dto.getPaymentNo())) {
                return ApiResponseDTO.success("取消付款成功", result);
            }
            return ApiResponseDTO.fail(400, result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "取消付款单失败：" + e.getMessage());
        }
    }

    @PostMapping("/query_by_payment_no")
    public ApiResponseDTO queryByPaymentNo(@RequestBody ForeignExchangePaymentQueryRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentQueryRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentQueryRequestDTO2VO(dto);
            // 调用应用服务查询付款单列表
            Page<ForeignExchangePaymentResponseVO> result = foreignExchangePaymentApplicationService.queryPaymentOrderList(vo);
            if (result != null && !CollectionUtils.isEmpty(result.getRecords())) {
                ForeignExchangePaymentResponseDTO responseDto = ForeignExchangePaymentConverter.foreignExchangePaymentResponseVO2DTO(result.getRecords().get(0));
                return ApiResponseDTO.success("查询成功", responseDto);
            }
            // 返回结果
            return ApiResponseDTO.success("查询失败", null);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询付款单详情失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出付款单列表为Excel
     */
    @PostMapping("/export")
    public void export(@RequestBody ForeignExchangePaymentQueryRequestDTO dto, HttpServletResponse response) {
        try {
            // 转换DTO为VO
            ForeignExchangePaymentQueryRequestVO vo = ForeignExchangePaymentConverter.foreignExchangePaymentQueryRequestDTO2VO(dto);
            // 设置分页参数，查询所有数据（上限10万条）
            vo.setPage(1L);
            vo.setSize(100000L);
            // 调用应用服务查询付款单列表
            Page<ForeignExchangePaymentResponseVO> result = foreignExchangePaymentApplicationService.queryPaymentOrderList(vo);
            
            // 转换为Excel VO列表
            List<ForeignExchangePaymentExcelVO> excelVOList = new ArrayList<>();
            for (ForeignExchangePaymentResponseVO payment : result.getRecords()) {
                ForeignExchangePaymentExcelVO excelVO = new ForeignExchangePaymentExcelVO();
                excelVO.setPaymentNo(payment.getPaymentNo());
                excelVO.setExecuteNo(payment.getOrderNo());
//                excelVO.setDirection(payment.get() == 1 ? "购汇" : "结汇");
                excelVO.setCurrency(payment.getPayCurrency());
                excelVO.setPaymentAmount(payment.getPaymentAmount());
                excelVO.setSubjectAccountNo(payment.getSubjectAccountNo());
                excelVO.setCounterpartyAccountNo(payment.getCounterpartyAccountNo());
                excelVO.setPaymentTime(payment.getPaymentTime());
                excelVO.setStatus(getStatusText(payment.getStatus()));
                excelVO.setCreateTime(payment.getCreateTime());
                excelVOList.add(excelVO);
            }
            
            // 导出Excel
            EasyExcelUtil.exportExcel(response, excelVOList, "外汇交易付款列表", ForeignExchangePaymentExcelVO.class);
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
            case 0:
                return "待支付";
            case 1:
                return "付款成功";
            case 2:
                return "付款失败";
            case 3:
                return "已取消";
            default:
                return "未知";
        }
    }

}