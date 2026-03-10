package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.application.service.AiChatService;
import com.example.foreign.exchange.application.service.ForeignExchangeApplyApplicationService;
import com.example.foreign.exchange.common.ai.OpenWebUiService;
import com.example.foreign.exchange.controller.converter.ForeignExchangeApplyConverter;
import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.common.ai.AliBaiLianService;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import com.example.foreign.exchange.controller.dto.ForeignExchangeApplyEditRequestDTO;
import com.example.foreign.exchange.common.utils.SseUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * AI 测试控制器
 * 用于测试阿里百炼服务的普通输出和流式输出功能
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AliBaiLianService aliBaiLianService;

    @Resource
    private AiChatService aiChatService;

    @Resource
    private OpenWebUiService openWebUiService;

    // API密钥
    private static final String API_KEY = "sk-101170a11741496fb8a30a3873ac5836";
    // API端点
    private static final String API_ENDPOINT = "http://localhost:3000/api/v1/messages";
    // JSON解析器
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 普通输出测试
     * @param prompt 提示词
     * @param systemPrompt 系统提示词（可选）
     * @return 生成的文本
     */
    @GetMapping("/generate")
    public ApiResponseDTO generateText(@RequestParam String prompt, @RequestParam(required = false) String systemPrompt) {
        try {
            // 调用阿里百炼服务的普通输出方法
            com.alibaba.dashscope.aigc.generation.GenerationResult result = AliBaiLianService.call(
                    systemPrompt != null ? systemPrompt : "你是一个智能助手",
                    prompt
            );
            
            // 提取文本内容
            String text = "";
            if (result.getOutput() != null && result.getOutput().getChoices() != null && !result.getOutput().getChoices().isEmpty()) {
                text = result.getOutput().getChoices().get(0).getMessage().getContent();
            }
            
            return ApiResponseDTO.success("111", text);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseDTO.fail(504, e.getMessage());
        }
    }

    /**
     * 流式输出测试
     * @param prompt 提示词
     * @param systemPrompt 系统提示词（可选）
     * @return SseEmitter，用于流式输出
     */
    @GetMapping("/stream")
    public SseEmitter generateTextStream(@RequestParam String prompt, @RequestParam(required = false) String systemPrompt) {
        // 创建 SSE 发射器
        SseEmitter emitter = aliBaiLianService.createSseEmitter();
        
        // 异步执行流式调用
        aliBaiLianService.streamCall(
                systemPrompt != null ? systemPrompt : "你是一个辅助外汇申请审批的智能助手（一定要专业的回答该外汇申请的内容），可以有标点符号，但不要有奇奇怪怪的符号",
                prompt,
                emitter
        );
        
        return emitter;
    }

    /**
     * 基于外汇申请的流式AI分析
     * @param request 外汇申请编辑请求DTO
     * @return SseEmitter，用于流式输出
     */
    @PostMapping("/stream/analyze")
    public SseEmitter streamAnalyzeForeignExchange(@RequestBody ForeignExchangeApplyEditRequestDTO request) {
        ForeignExchangeApplyEditRequestVO requestVO = ForeignExchangeApplyConverter.foreignExchangeApplyEditRequestDTO2VO(request);
        return aiChatService.streamCall(requestVO);
    }

    /**
     * 对话式聊天
     */
    @PostMapping("/stream/chat")
    public SseEmitter streamChat(@RequestParam String prompt) {
        return openWebUiService.streamCall(prompt);
    }

}