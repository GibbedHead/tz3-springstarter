package ru.chaplyginma.httploggingautoconfigure.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.chaplyginma.httplogging.logger.HttpLogger;
import ru.chaplyginma.httplogging.web.filter.WebHttpLoggingFilter;
import ru.chaplyginma.httplogging.web.logger.WebHttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "http-logging", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HttpLoggingProperties.class)
public class WebHttpLoggingAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public HttpLogger httpLogger() {
        return new HttpLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebHttpLogger webHttpLogger(HttpLogger httpLogger) {
        return new WebHttpLogger(httpLogger);
    }

    @Bean
    public OncePerRequestFilter httpLoggingFilter(
            HttpLoggingProperties httpLoggingProperties,
            WebHttpLogger webHttpLogger
    ) {
        return new WebHttpLoggingFilter(httpLoggingProperties, webHttpLogger);
    }

}
