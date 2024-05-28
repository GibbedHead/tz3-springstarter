package ru.chaplyginma.httploggingspringbootstarter.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.chaplyginma.httploggingautoconfigure.config.WebHttpLoggingAutoConfiguration;

@Configuration
@EnableAutoConfiguration
@Import(WebHttpLoggingAutoConfiguration.class)
public class WebHttpLoggingTestConfig {
}
