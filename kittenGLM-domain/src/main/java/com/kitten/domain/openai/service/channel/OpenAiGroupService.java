package com.kitten.domain.openai.service.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface OpenAiGroupService {

    void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws JsonProcessingException, Exception;

}
