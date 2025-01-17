package com.kitten.domain.openai.service;


import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ChatService extends AbstractChatService {

    @Resource
    private DefaultLogicFactory logicFactory;

    public ChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        super(chatGPTService, chatGLMService);
    }

    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountQuotaEntity userAccountQuotaEntity, String... logics) throws Exception {
        Map<String, ILogicFilter<UserAccountQuotaEntity>> logicFilterMap = logicFactory.openLogicFilter();
        RuleLogicEntity<ChatProcessAggregate> entity = null;
        for (String code : logics) {
            if (DefaultLogicFactory.LogicModel.NULL.getCode().equals(code)) continue;
            entity = logicFilterMap.get(code).filter(chatProcess, userAccountQuotaEntity);
            if (!LogicCheckTypeVO.SUCCESS.equals(entity.getType())) return entity;
        }
        return entity != null ? entity : RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
    }

}
