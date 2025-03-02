package com.kitten.domain.weixin.service.message;


import com.google.common.cache.Cache;
import com.kitten.dataTypes.exception.ChatGPTException;
import com.kitten.dataTypes.sdk.weixin.XmlUtil;
import com.kitten.domain.weixin.model.entity.MessageTextEntity;
import com.kitten.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.kitten.domain.weixin.model.valobj.MsgTypeVO;
import com.kitten.domain.weixin.service.IWeiXinBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 受理用户行为接口实现类
 */
@Slf4j
@Service
public class WeiXinBehaviorService implements IWeiXinBehaviorService {
    @Value("${wx.config.originalid}")
    private String originalId;

    @Resource
    private Cache<String, String> codeCache;

    /**
     * 1. 用户的请求行文，分为事件event、消息text，这里我们只处理消息内容
     * 2. 用户行为、消息类型，是多样性的，这部分如果用户有更多的扩展需求，可以使用设计模式【模板模式 + 策略模式 + 工厂模式】，分拆逻辑。
     */
    @Override
    public String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity) {
        // Event 事件类型, 忽略不处理
        if (MsgTypeVO.EVENT.getCode().equals(userBehaviorMessageEntity.getMsgType())){
            log.info("Event 事件类型, 忽略不处理");
            return "";
        }

        // Text 文本类型
        if (MsgTypeVO.TEXT.getCode().equals(userBehaviorMessageEntity.getMsgType())) {
            // 缓存验证码
            String isExistCode = codeCache.getIfPresent(userBehaviorMessageEntity.getOpenId());
            // 判断验证码
            if (StringUtils.isBlank(isExistCode)) {
                // 生成一个四位数的验证码
                String code = RandomStringUtils.randomNumeric(4);
                log.info("生成四位数验证码: {}", code);
                codeCache.put(code, userBehaviorMessageEntity.getOpenId());
                codeCache.put(userBehaviorMessageEntity.getOpenId(), code);
                isExistCode = code;
            }

            // 反馈信息[文本]
            MessageTextEntity res = new MessageTextEntity();
            res.setToUserName(userBehaviorMessageEntity.getOpenId());
            res.setFromUserName(originalId);
            res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
            res.setMsgType(MsgTypeVO.TEXT.getCode());
            res.setContent(String.format("您的验证码是:%s 有效期%d分钟", isExistCode, 3));
            return XmlUtil.beanToXml(res);
        }

        throw new ChatGPTException(userBehaviorMessageEntity.getMsgType() + " 未被处理的行为类型 Err！");

    }
}
