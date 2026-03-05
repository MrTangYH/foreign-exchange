package com.example.foreign.exchange.common.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.foreign.exchange.common.config.AliBaiLianConfig;
import com.example.foreign.exchange.common.utils.SseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 阿里百炼服务类
 * 用于调用阿里百炼的 API，支持普通输出和流式输出
 */
@Service
public class AliBaiLianService {

    private final AliBaiLianConfig aliBaiLianConfig;
    private final Generation generation;
    private static final Logger logger = LoggerFactory.getLogger(AliBaiLianService.class);

    @Autowired
    public AliBaiLianService(AliBaiLianConfig aliBaiLianConfig) {
        this.aliBaiLianConfig = aliBaiLianConfig;
        this.generation = new Generation();
    }

    /**
     * 流式输出
     */
    // 设置SSE超时时间（3分钟，可按需调整）
    private static final long SSE_TIMEOUT = 3 * 60 * 1000L;

    /**
     * 创建SSE发射器，供前端连接
     */
    public SseEmitter createSseEmitter() {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        // 连接超时/完成/异常时的回调
        emitter.onTimeout(() -> SseUtil.close(emitter));
        emitter.onCompletion(() -> logger.info("SSE连接正常关闭"));
        emitter.onError(e -> {
            logger.error("SSE连接异常", e);
            SseUtil.closeOnError(emitter, e);
        });
        return emitter;
    }

    /**
     * 异步执行流式调用，向前端推送结果
     */
    @Async // 异步执行，避免阻塞Tomcat线程（需在启动类加@EnableAsync）
    public void streamCall(String systemPrompt, String prompt, SseEmitter emitter) {
        try {
            Generation gen = new Generation();
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(systemPrompt + " " + prompt)
                    .build();
            streamCallWithMessage(gen, userMsg, emitter);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            logger.error("AI流式调用异常", e);
            SseUtil.sendMessage(emitter, "调用失败：" + e.getMessage());
            SseUtil.closeOnError(emitter, e);
        } catch (Exception e) {
            logger.error("未知异常", e);
            SseUtil.sendMessage(emitter, "系统异常：" + e.getMessage());
            SseUtil.closeOnError(emitter, e);
        }
    }

    private void streamCallWithMessage(Generation gen, Message userMsg, SseEmitter emitter)
            throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam param = buildGenerationParam(userMsg);
        // 流式调用返回Flowable（响应式流）
        gen.streamCall(param)
                .subscribe(
                        message -> handleGenerationResult(message, emitter),
                        error -> {
                            logger.error("流式调用错误", error);
                            SseUtil.sendMessage(emitter, "调用失败：" + error.getMessage());
                            SseUtil.closeOnError(emitter, error);
                        },
                        () -> {
                            logger.info("流式调用完成");
                            SseUtil.close(emitter);
                        }
                );
    }

    private GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                .apiKey(aliBaiLianConfig.getApiKey()) // 从配置文件读取 API Key
                .model(aliBaiLianConfig.getDefaultModel()) // 从配置文件读取模型
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true) // 增量输出（逐字/逐段返回）
                .build();
    }

    /**
     * 处理流式结果，推送给前端
     */
    private void handleGenerationResult(GenerationResult message, SseEmitter emitter) {
        if (message == null || message.getOutput() == null) {
            return;
        }
        
        // 提取增量文本（核心：每次只返回新增的内容）
        String incrementalText = null;
        
        // 尝试从 choices 中获取文本（当 resultFormat 为 MESSAGE 时）
        if (message.getOutput().getChoices() != null && !message.getOutput().getChoices().isEmpty()) {
            incrementalText = message.getOutput().getChoices().get(0).getMessage().getContent();
        }
        
        // 如果没有从 choices 中获取到文本，尝试直接从 output 中获取（当 resultFormat 为 TEXT 时）
        if (incrementalText == null && message.getOutput().getText() != null) {
            incrementalText = message.getOutput().getText();
        }
        
        if (incrementalText != null && !incrementalText.isEmpty()) {
            // 推送给前端
            SseUtil.sendMessage(emitter, incrementalText);
            // 可选：模拟延迟，让前端看到逐字效果（实际无需加）
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
//    private static void handleGenerationResult(GenerationResult message) {
//        System.out.println(JsonUtils.toJson(message));
//    }
//    public static void streamCallWithMessage(Generation gen, Message userMsg)
//            throws NoApiKeyException, ApiException, InputRequiredException {
//        GenerationParam param = buildGenerationParam(userMsg);
//        Flowable<GenerationResult> result = gen.streamCall(param);
//        result.blockingForEach(message -> handleGenerationResult(message));
//    }
//    private static GenerationParam buildGenerationParam(Message userMsg) {
//        return GenerationParam.builder()
//                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
//                .apiKey("sk-f86cc85bfe994a7e877ec8776d593381")
//                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
//                .model("qwen-plus")
//                .messages(Arrays.asList(userMsg))
//                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
//                .incrementalOutput(true)
//                .build();
//    }
//
//    public static GenerationResult streamCall(String systemPrompt, String prompt) throws ApiException, NoApiKeyException, InputRequiredException {
//        try {
//            Generation gen = new Generation();
//            Message userMsg = Message.builder().role(Role.USER.getValue()).content(systemPrompt + " " + prompt).build();
//            streamCallWithMessage(gen, userMsg);
//        } catch (ApiException | NoApiKeyException | InputRequiredException  e) {
//            logger.error("An exception occurred: {}", e.getMessage());
//        }
//    }
//    public static void main(String[] args) {
//        try {
//            Generation gen = new Generation();
//            Message userMsg = Message.builder().role(Role.USER.getValue()).content("你是谁？").build();
//            streamCallWithMessage(gen, userMsg);
//        } catch (ApiException | NoApiKeyException | InputRequiredException  e) {
//            logger.error("An exception occurred: {}", e.getMessage());
//        }
//        System.exit(0);
//    }

    /**
     * 普通输出
     */
    public static GenerationResult call(String systemPrompt, String prompt) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemPrompt)
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-f86cc85bfe994a7e877ec8776d593381")
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }

}