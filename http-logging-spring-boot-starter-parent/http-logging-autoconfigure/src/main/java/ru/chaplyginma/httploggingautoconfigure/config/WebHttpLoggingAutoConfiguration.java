package ru.chaplyginma.httploggingautoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.chaplyginma.httplogging.web.filter.WebHttpLoggingFilter;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "http-logging", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HttpLoggingProperties.class)
@Slf4j
public class WebHttpLoggingAutoConfiguration {

    @Bean
    public OncePerRequestFilter httpLoggingFilter(HttpLoggingProperties httpLoggingProperties) {
        log.info(" - WebHttpLoggingAutoConfiguration webHttpLoggingFilter");
        return new WebHttpLoggingFilter(httpLoggingProperties);
    }

}
