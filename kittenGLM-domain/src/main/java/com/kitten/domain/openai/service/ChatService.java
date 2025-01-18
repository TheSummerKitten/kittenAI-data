package com.kitten.domain.openai.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kitten.chatglmsdk.model.ChatCompletionRequest;
import com.kitten.chatglmsdk.model.Model;
import com.kitten.chatglmsdk.model.Role;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService extends AbstractChatService {

    @Override
    protected void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws JsonProcessingException {
        //1. 请求消息
        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages().stream()
                .map(entity -> ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content(entity.getContent())
                        .build()
                )
                .collect(Collectors.toList());

        //2. 封装参数
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Model.valueOf(chatProcess.getModel()))
                .prompt(prompts)
                .build();
        //3. 请求应答
        openAiSession.chatCompletions(chatCompletionRequest, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    Map<String, Object> map = objectMapper.readValue(data, Map.class);
                    responseBodyEmitter.send(map);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
}
