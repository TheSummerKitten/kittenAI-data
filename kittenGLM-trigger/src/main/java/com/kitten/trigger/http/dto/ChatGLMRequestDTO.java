package com.kitten.trigger.http.dto;

import com.kitten.dataTypes.enums.ChatGLMModel;
import com.kitten.domain.openai.model.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGLMRequestDTO {
    // 模型选择参数
    private String model = ChatGLMModel.GLM_4.getCode();
    // 对话内容: 用户角色, 提示符
    private List<MessageEntity> messages;

}
