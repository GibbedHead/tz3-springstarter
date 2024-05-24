package ru.chaplyginma.httploggingautoconfigure.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import ru.chaplyginma.httplogging.filter.HttpLoggingFilter;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
public class HttpLoggingAutoConfiguration {

    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    @ConditionalOnProperty(prefix = "http-logging", name = "enabled", havingValue = "true", matchIfMissing = true)
    public OncePerRequestFilter httpLoggingFilter(HttpLoggingProperties httpLoggingProperties) {
        return new HttpLoggingFilter(httpLoggingProperties);
    }

}
