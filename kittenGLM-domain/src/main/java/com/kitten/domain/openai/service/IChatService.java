package com.kitten.domain.openai.service;

import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface IChatService {

    ResponseBodyEmitter completions(ResponseBodyEmitter emitter ,ChatProcessAggregate chatProcess) throws Exception;
}
