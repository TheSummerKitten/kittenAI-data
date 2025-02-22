package com.kitten.dataTypes.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatGLMModel {
    CHATGLM_6B_SSE("chatGLM_6b_SSE"),
    CHATGLM_LITE("chatglm_lite"),
    CHATGLM_LITE_32K("chatglm_lite_32k"),
    CHATGLM_STD("chatglm_std"),
    CHATGLM_PRO("chatglm_pro"),
    GLM_4("glm-4"),
    GLM_4_FLASH("glm-4-flash"),
    ;
    private final String code;

    public static ChatGLMModel get(String code){
        switch (code){
            case "chatGLM_6b_SSE":
                return ChatGLMModel.CHATGLM_6B_SSE;
            case "chatglm_lite":
                return ChatGLMModel.CHATGLM_LITE;
            case "chatglm_lite_32k":
                return ChatGLMModel.CHATGLM_LITE_32K;
            case "chatglm_std":
                return ChatGLMModel.CHATGLM_STD;
            case "chatglm_pro":
                return ChatGLMModel.CHATGLM_PRO;
            case "glm-4":
                return ChatGLMModel.GLM_4;
            case "glm-4-flash":
            default:
                return ChatGLMModel.GLM_4_FLASH;
        }
    }

}
