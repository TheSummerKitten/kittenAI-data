package com.kitten.domain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kitten.chatglmsdk.session.OpenAiSession;
import com.kitten.dataTypes.common.Constants;
import com.kitten.dataTypes.enums.OpenAiChannel;
import com.kitten.dataTypes.exception.ChatGPTException;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.service.channel.OpenAiGroupService;
import com.kitten.domain.openai.service.channel.impl.ChatGLMService;
import com.kitten.domain.openai.service.channel.impl.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * glm 和 gpt的服务选择, 这里我们使用策略模式, 其实没有接入 gpt服务
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {


    @Override
    public ResponseBodyEmitter completions(ChatProcessAggregate chatProcess) throws Exception {
        // 1. 权限校验
        if (!"kitten".equals(chatProcess.getToken())) {
            throw new ChatGPTException(Constants.ResponseCode.TOKEN_ERROR.getCode(), Constants.ResponseCode.TOKEN_ERROR.getInfo());
        }
        log.info("校验通过");
        // 2. 请求应答
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        emitter.onCompletion(() -> {
            log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
        });
        emitter.onError(throwable -> log.error("流式问答请求疫情，使用模型：{}", chatProcess.getModel(), throwable));

        // 3. 应答处理
        log.info("开始处理请求");
        this.doMessageResponse(chatProcess, emitter);

        // 4. 返回结果
        return emitter;
    }

    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws Exception;

}
