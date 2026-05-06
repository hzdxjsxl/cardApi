package com.example.cardApi.controller;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ai")
public class AiController {

    private static final String API_KEY = "";
    private static final String API_URL = "";

    // 初始化 OkHttp 客户端
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @PostMapping("/analyze-stream")
    public SseEmitter analyzeStream(@RequestBody Map<String, Object> requestBody) {
        SseEmitter emitter = new SseEmitter(60000L);

        try {
            // 1. 组装提示词
            List<?> cards = (List<?>) requestBody.get("cards");
            String promptDesc = "用户抽到了以下塔罗牌：" + JSON.toJSONString(cards) + "。请用神秘、深邃的语气直接解析，不要废话。";
            Map<String, Object> llmRequest = new HashMap<>();
            llmRequest.put("model", "deepseek-chat");
            llmRequest.put("stream", true); // 开启流式输出
            llmRequest.put("messages", List.of(
                    Map.of("role", "user", "content", promptDesc)
            ));
            String jsonBody = JSON.toJSONString(llmRequest);
            okhttp3.MediaType mediaType = okhttp3.MediaType.get("application/json; charset=utf-8");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(jsonBody, mediaType);
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();
            EventSource.Factory factory = EventSources.createFactory(httpClient);
            factory.newEventSource(request, new EventSourceListener() {

                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    try {
                        if ("[DONE]".equals(data)) {
                            emitter.send(SseEmitter.event().data("[DONE]"));
                            emitter.complete();
                        } else {
                            emitter.send(SseEmitter.event().data("{\"text\": " + data + "}"));
                        }
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        eventSource.cancel();
                    }
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    emitter.complete();
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    System.err.println("大模型流式请求失败: " + t.getMessage());
                    emitter.completeWithError(t);
                }
            });

        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
