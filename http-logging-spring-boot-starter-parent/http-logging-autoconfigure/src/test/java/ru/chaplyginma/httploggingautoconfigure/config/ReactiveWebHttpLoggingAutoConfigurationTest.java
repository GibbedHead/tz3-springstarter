package ru.chaplyginma.httploggingautoconfigure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.chaplyginma.httplogging.logger.HttpLogger;
import ru.chaplyginma.httplogging.reactiveweb.filter.ReactiveWebHttpLoggingFilter;
import ru.chaplyginma.httplogging.reactiveweb.logger.ReactiveWebHttpLogger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class ReactiveWebHttpLoggingAutoConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ReactiveWebHttpLoggingAutoConfiguration.class))
            .withPropertyValues("http-logging.enabled=true");

    @Test
    void whenEnabledPropertyIsTrue_thenBeansAreRegistered() {
        contextRunner.run(context -> {
            String[] beanNames = context.getBeanDefinitionNames();
            assertThat(context).hasSingleBean(HttpLogger.class);
            assertThat(context).hasSingleBean(ReactiveWebHttpLogger.class);
            assertThat(context).hasSingleBean(ReactiveWebHttpLoggingFilter.class);
        });
    }

    @Test
    void whenEnabledPropertyIsFalse_thenBeansAreNotRegistered() {
        new ReactiveWebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ReactiveWebHttpLoggingAutoConfiguration.class))
                .withPropertyValues("http-logging.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HttpLogger.class);
                    assertThat(context).doesNotHaveBean(ReactiveWebHttpLogger.class);
                    assertThat(context).doesNotHaveBean(ReactiveWebHttpLoggingFilter.class);
                });
    }

    @Test
    void whenPropertyIsMissing_thenBeansAreRegisteredByDefault() {
        new ReactiveWebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ReactiveWebHttpLoggingAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasSingleBean(HttpLogger.class);
                    assertThat(context).hasSingleBean(ReactiveWebHttpLogger.class);
                    assertThat(context).hasSingleBean(ReactiveWebHttpLoggingFilter.class);
                });
    }

    @Test
    void whenNoWebflux_thenBeansAreNotRegistered() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ReactiveWebHttpLoggingAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HttpLogger.class);
                    assertThat(context).doesNotHaveBean(ReactiveWebHttpLogger.class);
                    assertThat(context).doesNotHaveBean(ReactiveWebHttpLoggingFilter.class);
                });
    }
}
