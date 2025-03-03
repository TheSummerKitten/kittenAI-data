package com.kitten.domain.openai.service;

import com.kitten.dataTypes.enums.OpenAiChannel;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.RuleLogicEntity;
import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import com.kitten.domain.openai.repository.IOpeAiRepository;
import com.kitten.domain.openai.service.channel.OpenAiGroupService;
import com.kitten.domain.openai.service.channel.impl.ChatGLMService;
import com.kitten.domain.openai.service.channel.impl.ChatGPTService;
import com.kitten.domain.openai.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * glm 和 gpt的服务选择, 这里我们使用策略模式, 其实没有接入 gpt服务
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {

    @Resource
    private IOpeAiRepository openAiRepository;

    private final Map<OpenAiChannel, OpenAiGroupService> openAiGroup = new HashMap<>();

    public AbstractChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        openAiGroup.put(OpenAiChannel.ChatGPT, chatGPTService);
        openAiGroup.put(OpenAiChannel.ChatGLM, chatGLMService);
    }

    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter,ChatProcessAggregate chatProcess) throws Exception {

        emitter.onCompletion(() -> {
            log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
        });
        emitter.onError(throwable -> log.error("流式问答请求疫情，使用模型：{}", chatProcess.getModel(), throwable));
        // 2. 用户账户获取
        UserAccountQuotaEntity userAccountQuotaEntity = openAiRepository.queryUserAccount(chatProcess.getOpenid());
        log.info("当前用户: {}", userAccountQuotaEntity);

        // 3. 规则过滤
        RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess,
                userAccountQuotaEntity,
                DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(),
                DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode(),
                null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.ACCOUNT_STATUS.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
                null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.MODEL_TYPE.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode(),
                null != userAccountQuotaEntity ? DefaultLogicFactory.LogicModel.USER_QUOTA.getCode() : DefaultLogicFactory.LogicModel.NULL.getCode());
        log.info("规则校验结果: {}", ruleLogicEntity.getType());
        if (!LogicCheckTypeVO.SUCCESS.equals(ruleLogicEntity.getType())) {
            emitter.send(ruleLogicEntity.getInfo());
            emitter.complete();
            return emitter;
        }


        // 3. 应答处理
        log.info("开始处理请求");
        openAiGroup.get(chatProcess.getChannel()).doMessageResponse(chatProcess, emitter);

        // 4. 返回结果
        return emitter;
    }

    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess,UserAccountQuotaEntity data, String... logics) throws Exception;



}
