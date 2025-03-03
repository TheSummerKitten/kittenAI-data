package com.kitten.domain.openai.service.rule;

import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;

/**
 * @author TheKitten
 * @description: 规则过滤接口
 */
public interface ILogicFilter<T> {

    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, T data) throws Exception;
}
