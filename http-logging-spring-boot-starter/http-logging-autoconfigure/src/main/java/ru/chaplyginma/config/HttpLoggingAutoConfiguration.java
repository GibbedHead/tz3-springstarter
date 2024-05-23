package ru.chaplyginma.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.chaplyginma.properties.HttpLoggingProperties;

@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
public class HttpLoggingAutoConfiguration {
}
