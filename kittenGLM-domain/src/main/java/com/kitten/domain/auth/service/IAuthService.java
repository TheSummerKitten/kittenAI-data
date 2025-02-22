package com.kitten.domain.auth.service;

import com.kitten.domain.auth.model.entity.AuthStateEntity;

public interface IAuthService {
    /**
     *
     * @param code 验证码
     * @return
     */
    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);
}
