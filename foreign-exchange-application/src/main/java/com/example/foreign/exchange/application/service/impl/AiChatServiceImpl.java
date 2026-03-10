package com.example.foreign.exchange.application.service.impl;

import com.example.foreign.exchange.application.entity.ForeignExchangeApplyEditRequestVO;
import com.example.foreign.exchange.application.service.AiChatService;
import com.example.foreign.exchange.common.ai.OpenWebUiService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class AiChatServiceImpl implements AiChatService {

    @Resource
    private OpenWebUiService openWebUiService;

    @Override
    public SseEmitter streamCall(ForeignExchangeApplyEditRequestVO requestVO) {
        String prompt = buildPrompt(requestVO);
        return openWebUiService.streamCall(prompt);
    }

    /**
     * 构建提示词
     * @param request 外汇申请编辑请求DTO
     * @return 构建的提示词
     */
    private String buildPrompt(ForeignExchangeApplyEditRequestVO request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请分析以下外汇申请信息，并提供专业的审批建议：\n");
        prompt.append("申请编号：").append(request.getApplyNo()).append("\n");
        prompt.append("交易方向：").append(request.getDirection() == 1 ? "购汇" : "结汇").append("\n");
        prompt.append("交易币种：").append(request.getCurrency()).append("\n");
        prompt.append("外币金额：").append(request.getAmount()).append("\n");
        prompt.append("汇率：").append(request.getRate()).append("\n");
        prompt.append("折算本币金额：").append(request.getRmbAmount()).append("\n");
        prompt.append("交易主体：").append(request.getTransactionSubject()).append("\n");
        prompt.append("主体账号：").append(request.getSubjectAccountNo()).append("\n");
        prompt.append("资金用途：").append(request.getPurpose()).append("\n");
        prompt.append("对手方名称：").append(request.getCounterparty()).append("\n");
        prompt.append("对手方账号：").append(request.getCounterpartyAccountNo()).append("\n");
        prompt.append("SWIFT/BIC 码：").append(request.getSwiftBic()).append("\n");
        return prompt.toString();
    }
}
