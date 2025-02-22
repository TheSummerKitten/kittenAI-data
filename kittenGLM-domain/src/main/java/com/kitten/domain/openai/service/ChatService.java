package com.kitten.domain.openai.service;


import com.alibaba.fastjson.JSON;
import com.kitten.chatglmsdk.model.*;
import com.kitten.chatglmsdk.session.OpenAiSession;
import com.kitten.dataTypes.enums.ChatGLMModel;
import com.kitten.dataTypes.exception.ChatGPTException;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.service.channel.impl.ChatGLMService;
import com.kitten.domain.openai.service.channel.impl.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService extends AbstractChatService {




    public ChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        super(chatGPTService, chatGLMService);
        log.info("ChatService init");
    }


    //    @Resource(name = "chatGLMOpenAiSession")
//    protected OpenAiSession chatGLMOpenAiSession;
//    @Override
//    protected void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception {
//        //1. 请求消息
//        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages().stream()
//                .map(entity -> ChatCompletionRequest.Prompt.builder()
//                        .role(Role.user.getCode())
//                        .content(entity.getContent())
//                        .build()
//                )
//                .collect(Collectors.toList());
//
//        //2. 封装参数
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .topP(0.7F)
//                .sseFormat("data")
//                .stream(true)
//                .incremental(false)
//                .isCompatible(true)
//                .temperature(0.9F)
//                .model(Model.valueOf(ChatGLMModel.get(chatProcess.getModel()).name()))
//                .prompt(prompts)
//                .build();
//        log.info("[请求参数] {}", JSON.toJSONString(chatCompletionRequest));
//        //3. 请求应答
//        chatGLMOpenAiSession.completions(chatCompletionRequest, new EventSourceListener() {
//            @Override
//            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
//                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
//                // 消息类型: add 增量，finish 结束，error 错误，interrupted 中断
//                if (EventType.add.getCode().equals(type)){
//                    try {
//                        emitter.send(response.getData());
//                        log.info("[输出增量] {}", JSON.toJSONString(response.getData()));
//                    } catch (Exception e) {
//                        throw new ChatGPTException(e.getMessage());
//                    }
//                }
//                if (EventType.finish.getCode().equals(type)) {
//                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
//                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
//                }
//            }
//            // 结束钩子
//            @Override
//            public void onClosed(EventSource eventSource) {
//                log.info("执行结束钩子");
//                emitter.complete();
//            }
//        });
//    }
}
