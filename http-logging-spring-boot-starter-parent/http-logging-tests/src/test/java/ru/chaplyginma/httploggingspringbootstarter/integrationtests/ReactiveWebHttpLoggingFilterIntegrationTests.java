package ru.chaplyginma.httploggingspringbootstarter.integrationtests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.chaplyginma.httploggingspringbootstarter.config.ReactiveWebHttpLoggingTestConfig;

@SpringBootApplication
@ExtendWith(SpringExtension.class)
@Import(ReactiveWebHttpLoggingTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveWebHttpLoggingFilterIntegrationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testLoggingFilter() {
        webTestClient.get()
                .uri("/any-url")
                .exchange()
                .expectStatus().isNotFound();


    }
}
