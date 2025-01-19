package com.kitten.app.config;

import com.kitten.chatglmsdk.session.OpenAiSession;
import com.kitten.chatglmsdk.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ChatGLMSDKConfigProperties.class)
public class ChatGLMSDKConfig {

    @Bean(name = "chatGLMOpenAiSession")
    @ConditionalOnProperty(value = "chatglm.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public OpenAiSession openAiSession(ChatGLMSDKConfigProperties properties) {
        com.kitten.chatglmsdk.session.Configuration configuration = new com.kitten.chatglmsdk.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setApiSecretKey(properties.getApiSecretKey());
        // 会话工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 开启会话
        return factory.openSession();
    }
}
