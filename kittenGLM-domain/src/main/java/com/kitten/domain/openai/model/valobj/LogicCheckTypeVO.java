package com.kitten.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 校验结果
 */
@Getter
@AllArgsConstructor
public enum LogicCheckTypeVO {
    SUCCESS("0000", "校验通过"),
    REFUSE("0001","校验拒绝"),
    ;

    private final String code;
    private final String info;
}
