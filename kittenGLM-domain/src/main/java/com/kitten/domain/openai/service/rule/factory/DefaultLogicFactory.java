package com.kitten.domain.openai.service.rule.factory;

import com.kitten.domain.openai.annotation.LogicStrategy;
import com.kitten.domain.openai.service.rule.ILogicFilter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TheKitten
 * @desc 默认规则工厂
 */
@Service
public class DefaultLogicFactory {
    // Java集合框架的ConcurrentHashMap类提供了线程安全的映射。 也就是说，多个线程可以一次访问该映射，而不会影响映射中条目的一致性。
    public Map<String, ILogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    // 获取规则
    public Map<String, ILogicFilter> openLogicFilter() {
        return logicFilterMap;
    }


    /**
     * 规则逻辑枚举
     */
    public enum LogicModel {

        ACCESS_LIMIT("ACCESS_LIMIT", "访问次数过滤"),
        SENSITIVE_WORD("SENSITIVE_WORD", "敏感词过滤"),
        ;

        private String code;
        private String info;

        LogicModel(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
