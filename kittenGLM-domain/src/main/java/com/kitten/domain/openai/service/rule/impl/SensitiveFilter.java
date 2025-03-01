package com.kitten.domain.openai.service.rule.impl;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.MessageEntity;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveFilter implements ILogicFilter {

    @Resource
    private SensitiveWordBs wordBs;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception {
        // 白名单用户不处理
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder().type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
        }
        ChatProcessAggregate newChatProcessAggregate = new ChatProcessAggregate();
        newChatProcessAggregate.setOpenid(chatProcess.getOpenid());
        newChatProcessAggregate.setModel(chatProcess.getModel());

        List<MessageEntity> newMessages = chatProcess.getMessages().stream().map(message -> {
            String content = message.getContent();
            String replace = wordBs.replace(content);
            return MessageEntity.builder()
                    .role(message.getRole())
                    .name(message.getName())
                    .content(replace)
                    .build();
        }).collect(Collectors.toList());
        newChatProcessAggregate.setMessages(newMessages);

        return RuleLogicEntity.<ChatProcessAggregate>builder().type(LogicCheckTypeVO.SUCCESS).data(newChatProcessAggregate).build();
    }
}
