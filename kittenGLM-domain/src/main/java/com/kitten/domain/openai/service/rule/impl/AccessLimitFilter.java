package com.kitten.domain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 访问次数限制filter
 * 实现:
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.ACCESS_LIMIT)
public class AccessLimitFilter implements ILogicFilter {
    @Value("${app.config.limit-count}")
    private Integer limitCount;
    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Resource
    private Cache<String, Integer> visitCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception {
        //1.判断是否白名单
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        String openid = chatProcess.getOpenid();

        //2.判断访问次数
        int visitCount = visitCache.get(openid, () -> 0);

        if (visitCount < limitCount) {
            visitCache.put(openid, visitCount + 1);
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您今日的" + limitCount + "次访问次数已经用完")
                .type(LogicCheckTypeVO.REFUSE)
                .data(chatProcess)
                .build();
    }
}
