package com.kitten.domain.openai.service.channel.impl;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kitten.chatglmsdk.session.OpenAiSession;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.service.channel.OpenAiGroupService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatGPTService implements OpenAiGroupService {

    @Resource
    protected OpenAiSession chatGPTOpenAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws JsonProcessingException {
        // 1. 请求消息
//        List<Message> messages = chatProcess.getMessages().stream()
//                .map(entity -> Message.builder()
//                        .role(Constants.Role.valueOf(entity.getRole().toUpperCase()))
//                        .content(entity.getContent())
//                        .name(entity.getName())
//                        .build())
//                .collect(Collectors.toList());

        // 2. 封装参数
//        ChatCompletionRequest chatCompletion = ChatCompletionRequest
//                .builder()
//                .stream(true)
//                .messages(messages)
//                .model(chatProcess.getModel())
//                .build();

        // 3.2 请求应答
//        chatGPTOpenAiSession.chatCompletions(chatCompletion, new EventSourceListener() {
//            @Override
//            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
//                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
//                List<ChatChoice> choices = chatCompletionResponse.getChoices();
//                for (ChatChoice chatChoice : choices) {
//                    Message delta = chatChoice.getDelta();
//                    if (Constants.Role.ASSISTANT.getCode().equals(delta.getRole())) continue;
//
//                    // 应答完成
//                    String finishReason = chatChoice.getFinishReason();
//                    if (StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)) {
//                        emitter.complete();
//                        break;
//                    }
//
//                    // 发送信息
//                    try {
//                        emitter.send(delta.getContent());
//                    } catch (Exception e) {
//                        throw new ChatGPTException(e.getMessage());
//                    }
//                }
//            }
//        });
        log.info("GPT 服务未实现");
    }

}
