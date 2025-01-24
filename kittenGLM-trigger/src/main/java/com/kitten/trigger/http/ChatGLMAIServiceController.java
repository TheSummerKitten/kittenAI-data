package com.kitten.trigger.http;

import com.alibaba.fastjson.JSON;
import com.kitten.dataTypes.common.Constants;
import com.kitten.domain.openai.model.aggregates.ChatProcessAggregate;
import com.kitten.domain.openai.model.entity.MessageEntity;
import com.kitten.domain.openai.service.IChatService;
import com.kitten.trigger.http.dto.ChatGLMRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/${app.config.api-version}/chatgpt/")
public class ChatGLMAIServiceController {
    @Resource
    private IChatService chatService;

    @PostMapping("completions")
    public ResponseBodyEmitter completionsStream(
            @RequestBody ChatGLMRequestDTO request,
            @RequestHeader("Authorization") String token,
            HttpServletResponse response) throws Exception {
        log.info("流式问答请求开始, 使用模型: {} | 提示符: {}", request.getModel(), JSON.toJSONString(request.getMessages()) );
        log.info("用户token: {}", token);
        //1. 基础配置
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        //2. 构建异步响应对象
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        //3. 鉴权
//        boolean success = authService.checkToken(token);
        if (!token.equals("kitten")) { // !success
            try {
                emitter.send(Constants.ResponseCode.TOKEN_ERROR.getCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
            return emitter;
        }


        //2. 构建参数
        ChatProcessAggregate chatProcessAggregate = ChatProcessAggregate.builder()
                .token(token)
                .model(request.getModel())
                .messages(request.getMessages().stream().map(entity -> MessageEntity.builder()
                        .role(entity.getRole())
                        .content(entity.getContent())
                        .build())
                        .collect(Collectors.toList()))
                .build();
        //3. 返回
        return chatService.completions(emitter, chatProcessAggregate);
    }

    @PostMapping("test")
    public String test(@RequestParam String params) {
        return "success test" + params;
    }

}
