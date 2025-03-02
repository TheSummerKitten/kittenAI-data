package com.kitten.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@Slf4j
@SpringBootApplication
@ComponentScan({"com.kitten.trigger", "com.kitten.domain", "com.kitten.app"})
@Configurable
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("❤❤❤ChatGLM-App 启动成功❤❤❤");
    }
}
