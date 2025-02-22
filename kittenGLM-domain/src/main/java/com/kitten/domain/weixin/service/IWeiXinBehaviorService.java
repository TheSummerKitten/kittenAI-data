package com.kitten.domain.weixin.service;


import com.kitten.domain.weixin.model.entity.UserBehaviorMessageEntity;

/**
 * 受理用户行为接口
 * @author kitten
 */
public interface IWeiXinBehaviorService {

    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);
}
