package com.kitten.domain.openai.service;

import com.kitten.dataTypes.enums.OpenAiChannel;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.service.channel.OpenAiGroupService;
import com.kitten.domain.openai.service.channel.impl.ChatGLMService;
import com.kitten.domain.openai.service.channel.impl.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * glm 和 gpt的服务选择, 这里我们使用策略模式, 其实没有接入 gpt服务
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {

    private final Map<OpenAiChannel, OpenAiGroupService> openAiGroup = new HashMap<>();

    public AbstractChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        openAiGroup.put(OpenAiChannel.ChatGPT, chatGPTService);
        openAiGroup.put(OpenAiChannel.ChatGLM, chatGLMService);
    }

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter,ChatProcessAggregate chatProcess) throws Exception {

        emitter.onCompletion(() -> {
            log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
        });
        emitter.onError(throwable -> log.error("流式问答请求疫情，使用模型：{}", chatProcess.getModel(), throwable));

        // 3. 应答处理
        log.info("开始处理请求");
        openAiGroup.get(chatProcess.getChannel()).doMessageResponse(chatProcess, emitter);

        // 4. 返回结果
        return emitter;
    }


}
