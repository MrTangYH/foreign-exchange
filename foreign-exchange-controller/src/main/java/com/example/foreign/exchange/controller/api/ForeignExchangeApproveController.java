package com.example.foreign.exchange.controller.api;

import com.example.foreign.exchange.common.ai.AliBaiLianService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ForeignExchangeApproveController {

    @Resource
    private AliBaiLianService aliBaiLianService;

    /**
     * 流式AI回答接口
     * @param systemPrompt 系统提示词（可选）
     * @param prompt 用户提问
     * @return SseEmitter 流式响应
     */
    @GetMapping("/stream-answer")
    public SseEmitter streamAnswer(
            @RequestParam(required = false, defaultValue = "") String systemPrompt,
            @RequestParam String prompt
    ) {
        // 创建SSE发射器
        SseEmitter emitter = aliBaiLianService.createSseEmitter();
        // 异步执行流式调用
        aliBaiLianService.streamCall(systemPrompt, prompt, emitter);
        return emitter;
    }
}
