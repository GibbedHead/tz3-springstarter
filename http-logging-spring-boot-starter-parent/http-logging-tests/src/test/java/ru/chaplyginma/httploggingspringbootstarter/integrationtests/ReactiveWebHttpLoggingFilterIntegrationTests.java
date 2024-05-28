package ru.chaplyginma.httploggingspringbootstarter.integrationtests;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.chaplyginma.httploggingspringbootstarter.config.ReactiveWebHttpLoggingTestConfig;
import ru.chaplyginma.httploggingspringbootstarter.logger.MemoryAppender;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootApplication
@ExtendWith(SpringExtension.class)
@Import(ReactiveWebHttpLoggingTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveWebHttpLoggingFilterIntegrationTests {
    private static final String LOGGER_NAME = "ru.chaplyginma.httplogging.logger.HttpLogger";
    private static final String MSG = "Request:";
    private static MemoryAppender memoryAppender;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();

    }

    @AfterEach
    public void cleanUp() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    public void testLoggingFilter() {
        webTestClient.get()
                .uri("/any-url")
                .exchange()
                .expectStatus().isNotFound();

        assertThat(memoryAppender.search(MSG, Level.INFO).size()).isEqualTo(1);
    }
}
