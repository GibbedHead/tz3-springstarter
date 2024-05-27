package ru.chaplyginma.httploggingautoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.chaplyginma.httplogging.reactiveweb.filter.ReactiveWebHttpLoggingFilter;
import ru.chaplyginma.httplogging.reactiveweb.logger.HttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(prefix = "http-logging", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HttpLoggingProperties.class)
@Slf4j
public class ReactiveWebHttpLoggingAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public HttpLogger httpLogger() {
        return new HttpLogger();
    }

    @Bean
    public ReactiveWebHttpLoggingFilter reactiveWebHttpLoggingFilter(
            HttpLoggingProperties httpLoggingProperties,
            HttpLogger httpLogger
    ) {
        log.info(" - ReactiveWebHttpLoggingAutoConfiguration reactiveWebHttpLoggingFilter");
        return new ReactiveWebHttpLoggingFilter(httpLoggingProperties, httpLogger);
    }
}
