package com.kitten.trigger.http;

import com.kitten.domain.openai.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin")
@RequestMapping("/api/${app.config.api-version}/chatgpt/")
public class ChatGPTAIServiceController {
    @Resource
    private IChatService chatService;
}
