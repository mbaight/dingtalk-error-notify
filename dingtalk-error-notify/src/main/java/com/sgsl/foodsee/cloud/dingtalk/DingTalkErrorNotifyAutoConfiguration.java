package com.sgsl.foodsee.cloud.dingtalk;

import com.sgsl.foodsee.cloud.errorhandler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * @author liuyo on 17.10.13.
 */
@Configuration
@ConditionalOnProperty(prefix = DingTalkProperties.PROPERTIES_PREFIX, name = "api-url")
@EnableConfigurationProperties(DingTalkProperties.class)
@Slf4j
public class DingTalkErrorNotifyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkErrorNotifier dingTalkNotifier(DingTalkProperties properties, RestTemplate restTemplate, ServerProperties serverProperties, Environment environment) {
        log.debug("startting DingTalkErrorNotifier");
        return new DingTalkErrorNotifier(restTemplate, properties, serverProperties, environment);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DingTalkProperties.PROPERTIES_PREFIX, name = "api-url")
    public GlobalExceptionHandler globalExceptionHandler(DingTalkErrorNotifier dingTalkErrorNotifier){
        return new GlobalExceptionHandler(dingTalkErrorNotifier);
    }
}

