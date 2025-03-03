package com.kitten.domain.openai.service.rule.impl;

import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.model.valobj.UserAccountStatusVO;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 账户校验
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.ACCOUNT_STATUS)
public class AccountStatusFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountQuotaEntity data) throws Exception {
        log.info("用户账户状态校验: {}", data.getUserAccountStatusVO());
        // 账户可用, 直接放行
        if (UserAccountStatusVO.AVAILABLE.equals(data.getUserAccountStatusVO())) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您的账户已被冻结, 请联系客服解冻后再使用。")
                .type(LogicCheckTypeVO.REFUSE)
                .data(chatProcess)
                .build();

    }
}
