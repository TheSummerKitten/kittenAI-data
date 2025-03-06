package com.kitten.app.config;

import com.google.common.eventbus.EventBus;
import com.kitten.trigger.http.listener.OrderPaySuccessListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuavaEventBusConfig {

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener) {
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }

}
