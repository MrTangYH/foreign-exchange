package com.example.foreign.exchange.application.service;

import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AiChatService {

    public SseEmitter streamCall(ForeignExchangeApplyEditRequestVO requestVO);
}
