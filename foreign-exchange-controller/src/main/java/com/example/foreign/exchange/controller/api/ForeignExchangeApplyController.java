package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyRequestVO;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyResponseVO;
import com.example.foreign.exchange.application.service.ForeignExchangeApplyApplicationService;
import com.example.foreign.exchange.common.entity.Page;
import com.example.foreign.exchange.common.utils.EasyExcelUtil;
import com.example.foreign.exchange.controller.converter.ForeignExchangeApplyConverter;
import com.example.foreign.exchange.controller.dto.*;
import com.example.foreign.exchange.controller.vo.ForeignExchangeApplyExcelVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 外汇申请模块
 */
@RestController
@RequestMapping("/apply")
public class ForeignExchangeApplyController {
    
    @Resource
    private ForeignExchangeApplyApplicationService foreignExchangeApplyApplicationService;

    /**
     * 外汇申请草稿保存
     */
    @PostMapping("/submit_draft")
    public ApiResponseDTO submitDraft(@RequestBody ForeignExchangeApplyRequestDTO dto) {
        try {
            // 直接调用应用服务的方法，传入DTO
            ForeignExchangeApplyRequestVO vo = ForeignExchangeApplyConverter.foreignExchangeApplyRequestDTO2VO(dto);
            String applyNo = foreignExchangeApplyApplicationService.saveDraft(vo);
            
            // 返回结果
            return ApiResponseDTO.success("保存成功", applyNo
            );
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "保存失败：" + e.getMessage());
        }
    }

    /**
     * 查询外汇申请列表
     */
    @PostMapping("/query_list")
    public ApiResponseDTO queryList(@RequestBody ForeignExchangeApplyEditRequestDTO dto) {
        try {
            // 直接调用应用服务的方法，传入DTO
            ForeignExchangeApplyEditRequestVO vo = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestDTO2VO(dto);
            Page<ForeignExchangeApplyResponseVO> result = foreignExchangeApplyApplicationService.queryApplyList(vo);
            
            // 转换数据
            Page<ForeignExchangeApplyResponseDTO> responsePage = new Page<>();
            responsePage.setTotal(result.getTotal());
            responsePage.setCurrent(result.getCurrent());
            responsePage.setSize(result.getSize());
            responsePage.setPages(result.getPages());
            
            // 转换每条记录
            java.util.List<ForeignExchangeApplyResponseDTO> responseList = new java.util.ArrayList<>();
            for (ForeignExchangeApplyResponseVO apply : result.getRecords()) {
                responseList.add(ForeignExchangeApplyConverter.foreignExchangeApplyToResponseDTO(apply));
            }
            responsePage.setRecords(responseList);
            
            // 返回结果
            return ApiResponseDTO.success("查询成功", responsePage);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询外汇申请详情
     */
    @PostMapping("/query_by_apply_no")
    public ApiResponseDTO queryByApplyNo(@RequestBody ForeignExchangeApplyEditRequestDTO dto) {
        try {
            // 直接调用应用服务的方法，传入DTO
            ForeignExchangeApplyEditRequestVO vo = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestDTO2VO(dto);
            Page<ForeignExchangeApplyResponseVO> result = foreignExchangeApplyApplicationService.queryApplyList(vo);

            if (CollectionUtils.isEmpty(result.getRecords())){
                return ApiResponseDTO.fail(404, "未查询到该申请");
            }
            // 返回结果
            return ApiResponseDTO.success("查询成功", result.getRecords().get(0));
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "查询失败：" + e.getMessage());
        }
    }

    /**
     * 编辑外汇申请草稿
     */
    @PostMapping("/edit_draft")
    public ApiResponseDTO editDraft(@RequestBody ForeignExchangeApplyEditRequestDTO dto) {
        try {
            // 转换DTO为VO
            ForeignExchangeApplyEditRequestVO vo = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestDTO2VO(dto);
            // 调用应用服务编辑草稿
            String applyNo = foreignExchangeApplyApplicationService.editDraft(vo);
            // 返回结果
            return ApiResponseDTO.success("编辑成功", applyNo);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "编辑失败：" + e.getMessage());
        }
    }

    /**
     * 变更外汇申请状态
     */
    @PostMapping("/change_status")
    public ApiResponseDTO changeStatus(@RequestBody ForeignExchangeApplyStatusRequestDTO dto) {
        try {
            // 调用应用服务变更状态
            String result = foreignExchangeApplyApplicationService.changeStatus(dto.getApplyNo(), dto.getStatus());
            // 返回结果
            return ApiResponseDTO.success("状态变更成功", result);
        } catch (Exception e) {
            return ApiResponseDTO.fail(500, "状态变更失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出外汇申请列表为Excel
     */
    @PostMapping("/export")
    public void export(@RequestBody ForeignExchangeApplyEditRequestDTO dto, HttpServletResponse response) {
        try {
            // 转换DTO为VO
            ForeignExchangeApplyEditRequestVO vo = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestDTO2VO(dto);
            // 设置分页参数，查询所有数据（上限10万条）
            vo.setPage(1L);
            vo.setSize(100000L);
            // 调用应用服务查询申请列表
            Page<ForeignExchangeApplyResponseVO> result = foreignExchangeApplyApplicationService.queryApplyList(vo);
            
            // 转换为Excel VO列表
            List<ForeignExchangeApplyExcelVO> excelVOList = new ArrayList<>();
            for (ForeignExchangeApplyResponseVO apply : result.getRecords()) {
                ForeignExchangeApplyExcelVO excelVO = new ForeignExchangeApplyExcelVO();
                excelVO.setApplyNo(apply.getApplyNo());
                excelVO.setUserId(apply.getUserId());
                excelVO.setDirection(apply.getDirection() == 1 ? "购汇" : "结汇");
                excelVO.setCurrency(apply.getCurrency());
                excelVO.setAmount(apply.getAmount());
                excelVO.setRate(apply.getRate());
                excelVO.setRmbAmount(apply.getRmbAmount());
                excelVO.setTransactionSubject(apply.getTransactionSubject());
                excelVO.setSubjectAccountNo(apply.getSubjectAccountNo());
                excelVO.setPurpose(apply.getPurpose());
                excelVO.setCounterparty(apply.getCounterparty());
                excelVO.setCounterpartyAccountNo(apply.getCounterpartyAccountNo());
                excelVO.setSwiftBic(apply.getSwiftBic());
                excelVO.setStatus(getStatusText(apply.getStatus()));
                excelVO.setSubmitTime(apply.getSubmitTime());
                excelVO.setCreateTime(apply.getCreateTime());
                excelVOList.add(excelVO);
            }
            
            // 导出Excel
            EasyExcelUtil.exportExcel(response, excelVOList, "外汇申请列表", ForeignExchangeApplyExcelVO.class);
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
                return "草稿";
            case 1:
                return "待审核";
            case 2:
                return "已审核";
            case 3:
                return "已拒绝";
            case 4:
                return "已作废";
            default:
                return "未知";
        }
    }
}