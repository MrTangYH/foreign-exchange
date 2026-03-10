package com.example.foreign.exchange.common.ai;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.foreign.exchange.common.config.OpenWebUiConfig;
import com.example.foreign.exchange.common.utils.SseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class OpenWebUiService {

    @Resource
    private OpenWebUiConfig openWebUiConfig;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * open web ui流式响应
     * @param prompt
     * @return
     */
    public SseEmitter streamCall(String prompt) {
        SseEmitter emitter = new SseEmitter();
        // 异步执行流式调用
        new Thread(() -> {
            try {
                // 构建提示词

                // 构建请求体
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", openWebUiConfig.getDefaultModel());
                requestBody.put("max_tokens", 1024);
                requestBody.put("stream", true);
                requestBody.put("rag", true);
//                requestBody.put("knowledge_base", "your_knowledge_base_name");

                // 构建messages数组
                JSONArray messages = new JSONArray();
                JSONObject message = new JSONObject();
                message.put("role", "user");
                message.put("content", prompt);
                messages.add(message);
                requestBody.put("messages", messages);

                // 转换为字符串
                String requestBodyString = requestBody.toString();

                // 创建URL对象
                URL url = new URL(openWebUiConfig.getUrl());
                // 打开连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // 设置请求方法
                connection.setRequestMethod("POST");
                // 设置请求头
                connection.setRequestProperty("x-api-key", openWebUiConfig.getApiKey());
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "text/event-stream");
                // 允许输入输出
                connection.setDoOutput(true);
                connection.setDoInput(true);
                // 禁用缓存
                connection.setUseCaches(false);

                // 发送请求
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = requestBodyString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // 处理响应
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 读取流式响应
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // 处理每一行响应
                            if (!line.isEmpty()) {
                                // 解析响应并提取文本内容
                                String text = parseStreamResponse(line);
                                if (text != null && !text.isEmpty()) {
                                    // 发送到SSE
                                    SseUtil.sendMessage(emitter, text);
                                }
                            }
                        }
                    }
                } else {
                    // 读取错误响应
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        String line;
                        StringBuilder error = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            error.append(line);
                        }
                        SseUtil.sendMessage(emitter, "Error: " + error.toString());
                    }
                }

                // 关闭SSE连接
                SseUtil.close(emitter);

            } catch (Exception e) {
                e.printStackTrace();
                SseUtil.sendMessage(emitter, "Error: " + e.getMessage());
                SseUtil.closeOnError(emitter, e);
            }
        }).start();

        return emitter;
    }

    /**
     * 解析流式响应中的JSON数据
     * @param line 响应行
     * @return 解析后的文本内容
     */
    private String parseStreamResponse(String line) {
        try {
            // 处理data:开头的行
            if (line.startsWith("data: ")) {
                String jsonStr = line.substring(6);
                // 解析JSON
                JsonNode rootNode = objectMapper.readTree(jsonStr);
                // 提取text字段
                if (rootNode.has("delta") && rootNode.get("delta").has("text")) {
                    return rootNode.get("delta").get("text").asText();
                }
            }
        } catch (Exception e) {
            // 解析错误时忽略
        }
        return null;
    }
}
