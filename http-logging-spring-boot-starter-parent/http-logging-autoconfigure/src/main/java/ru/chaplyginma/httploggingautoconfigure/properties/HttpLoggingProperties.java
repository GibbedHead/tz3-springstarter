package ru.chaplyginma.httploggingautoconfigure.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "http-logging")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HttpLoggingProperties {
    Boolean enabled;
}
