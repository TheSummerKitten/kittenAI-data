package com.kitten.infrastructure.dao;


import com.kitten.infrastructure.po.UserAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账户DAO
 */
@Mapper
public interface IUserAccountDao {
    int subAccountQuota(String openid);

    UserAccountPO queryUserAccount(String openid);
}
