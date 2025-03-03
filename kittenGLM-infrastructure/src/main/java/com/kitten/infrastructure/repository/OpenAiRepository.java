package com.kitten.infrastructure.repository;

import com.kitten.domain.openai.model.entity.UserAccountQuotaEntity;
import com.kitten.domain.openai.model.valobj.UserAccountStatusVO;
import com.kitten.domain.openai.repository.IOpeAiRepository;
import com.kitten.infrastructure.dao.IUserAccountDao;
import com.kitten.infrastructure.po.UserAccountPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OpenAiRepository implements IOpeAiRepository {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public int subAccountQuota(String openai) {
        return userAccountDao.subAccountQuota(openai);
    }

    @Override
    public UserAccountQuotaEntity queryUserAccount(String openid) {
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if (null == userAccountPO) {
            return null;
        }
        UserAccountQuotaEntity userAccountQuotaEntity = new UserAccountQuotaEntity();
        userAccountQuotaEntity.setOpenid(userAccountPO.getOpenid());
        userAccountQuotaEntity.setTotalQuota(userAccountPO.getTotalQuota());
        userAccountQuotaEntity.setSurplusQuota(userAccountPO.getSurplusQuota());
        userAccountQuotaEntity.setUserAccountStatusVO(UserAccountStatusVO.get(userAccountPO.getStatus()));
        userAccountQuotaEntity.genModelTypes(userAccountPO.getModelTypes());
        return userAccountQuotaEntity;
    }
}
