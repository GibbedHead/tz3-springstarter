package ru.chaplyginma.httploggingautoconfigure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.chaplyginma.httplogging.logger.HttpLogger;
import ru.chaplyginma.httplogging.web.filter.WebHttpLoggingFilter;
import ru.chaplyginma.httplogging.web.logger.WebHttpLogger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class WebHttpLoggingAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WebHttpLoggingAutoConfiguration.class))
            .withPropertyValues("http-logging.enabled=true");

    @Test
    void whenEnabledPropertyIsTrue_thenBeansAreRegistered() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(HttpLogger.class);
            assertThat(context).hasSingleBean(WebHttpLogger.class);
            assertThat(context).hasSingleBean(WebHttpLoggingFilter.class);
        });
    }

    @Test
    void whenEnabledPropertyIsFalse_thenBeansAreNotRegistered() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(WebHttpLoggingAutoConfiguration.class))
                .withPropertyValues("http-logging.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HttpLogger.class);
                    assertThat(context).doesNotHaveBean(WebHttpLogger.class);
                    assertThat(context).doesNotHaveBean(WebHttpLoggingFilter.class);
                });
    }

    @Test
    void whenPropertyIsMissing_thenBeansAreRegisteredByDefault() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(WebHttpLoggingAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasSingleBean(HttpLogger.class);
                    assertThat(context).hasSingleBean(WebHttpLogger.class);
                    assertThat(context).hasSingleBean(WebHttpLoggingFilter.class);
                });
    }

    @Test
    void whenNoWeb_thenBeansAreNotRegistered() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(WebHttpLoggingAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HttpLogger.class);
                    assertThat(context).doesNotHaveBean(WebHttpLogger.class);
                    assertThat(context).doesNotHaveBean(WebHttpLoggingFilter.class);
                });
    }
}