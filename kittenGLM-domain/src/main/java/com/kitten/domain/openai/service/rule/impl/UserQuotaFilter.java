package com.kitten.domain.openai.service.rule.impl;

import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.repository.IOpeAiRepository;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户额度校验
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.USER_QUOTA)
public class UserQuotaFilter implements ILogicFilter<UserAccountQuotaEntity> {
    @Resource
    private IOpeAiRepository openAiRepository;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        log.info("用户额度校验: {} -- surPlus: {}", data.getTotalQuota(), data.getSurplusQuota());
        if (data.getSurplusQuota() > 0) {
            // 扣减额度, 由于无资源竞争, 所以直接使用数据库也可|redis也好
            int updateCount = openAiRepository.subAccountQuota(data.getOpenid());
            log.info("扣减额度结果: {}", updateCount);
            if (0 != updateCount) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicCheckTypeVO.SUCCESS)
                        .data(chatProcess)
                        .build();
            }

        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您的额度已经用完。")
                .type(LogicCheckTypeVO.REFUSE)
                .data(chatProcess)
                .build();
    }
}
