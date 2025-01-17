package com.kitten.domain.openai.model.entity;

import com.kitten.domain.openai.model.valobj.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleLogicEntity<T> {
    private LogicCheckTypeVO type;
    private String info;
    private T data;
}
