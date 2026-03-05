package com.example.foreign.exchange.common.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class SseUtil {
    // 发送流式消息
    public static void sendMessage(SseEmitter emitter, String message) {
        try {
            // 发送纯文本（也可封装成JSON）
            emitter.send(SseEmitter.event().data(message));
        } catch (IOException e) {
            log.error("SSE发送消息失败", e);
            throw new RuntimeException(e);
        }
    }

    // 关闭SSE连接
    public static void close(SseEmitter emitter) {
        if (emitter != null) {
            emitter.complete();
        }
    }

    // 异常关闭SSE连接
    public static void closeOnError(SseEmitter emitter, Throwable e) {
        if (emitter != null) {
            emitter.completeWithError(e);
        }
    }
}
