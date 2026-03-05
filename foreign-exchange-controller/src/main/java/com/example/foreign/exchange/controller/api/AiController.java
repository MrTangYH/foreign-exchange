package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.common.ai.AliBaiLianService;
import com.example.foreign.exchange.controller.dto.ApiResponseDTO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.TimeUnit;

/**
 * AI 测试控制器
 * 用于测试阿里百炼服务的普通输出和流式输出功能
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AliBaiLianService aliBaiLianService;



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
}