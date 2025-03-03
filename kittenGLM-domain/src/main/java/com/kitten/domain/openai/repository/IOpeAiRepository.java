package com.kitten.domain.openai.repository;

import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;

/**
 * OpenAi 仓储接口
 */

public interface IOpeAiRepository {

    int subAccountQuota(String openai);

    UserAccountQuotaEntity queryUserAccount(String openid);
}
