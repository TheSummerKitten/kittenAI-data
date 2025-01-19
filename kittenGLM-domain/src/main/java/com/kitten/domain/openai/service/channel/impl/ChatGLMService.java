package com.kitten.domain.openai.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kitten.chatglmsdk.model.*;
import com.kitten.chatglmsdk.session.OpenAiSession;
import com.kitten.dataTypes.enums.ChatGLMModel;
import com.kitten.dataTypes.exception.ChatGPTException;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.service.channel.OpenAiGroupService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ChatGLMService implements OpenAiGroupService {
    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws JsonProcessingException, Exception {
        log.info("GLM方案未实现");
    }
    //    @Resource
//    protected OpenAiSession chatGlMOpenAiSession;
//
//    @Override
//    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception {
//        // 1. 请求消息
//        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages().stream()
//                .map(entity -> ChatCompletionRequest.Prompt.builder()
//                        .role(Role.user.getCode())
//                        .content(entity.getContent())
//                        .build())
//                .collect(Collectors.toList());
//
//        // 2. 封装参数
//        ChatCompletionRequest request = new ChatCompletionRequest();
//        request.setModel(Model.valueOf(ChatGLMModel.get(chatProcess.getModel()).name())); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
//        request.setPrompt(prompts);
//
//        chatGlMOpenAiSession.completions(request, new EventSourceListener() {
//            @Override
//            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
//                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
//
//                // 发送信息
//                if (EventType.add.getCode().equals(type)){
//                    try {
//                        emitter.send(response.getData());
//                    } catch (Exception e) {
//                        throw new ChatGPTException(e.getMessage());
//                    }
//                }
//                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
//                if (EventType.finish.getCode().equals(type)) {
//                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
//                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
//                }
//            }
//            @Override
//            public void onClosed(EventSource eventSource) {
//                emitter.complete();
//            }
//        });
//
//    }

}
