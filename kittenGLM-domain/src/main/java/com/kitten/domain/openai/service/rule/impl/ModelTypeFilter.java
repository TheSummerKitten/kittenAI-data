package com.kitten.domain.openai.service.rule.impl;


import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.MODEL_TYPE)
public class ModelTypeFilter implements ILogicFilter<UserAccountQuotaEntity> {
    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        log.info("用户可用模型校验: {} -- 当前用户模型:{}", data.getAllowModelTypeList(), chatProcess.getModel());
        List<String> allowModelTypeList = data.getAllowModelTypeList();
        String model = chatProcess.getModel();  // 获取当前用户使用模型
        // 模型校验
        if (allowModelTypeList.contains(model)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.REFUSE)
                .info("您的模型不可用, 请联系客服处理。")
                .data(chatProcess)
                .build();
    }
}
