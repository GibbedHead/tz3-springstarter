package ru.chaplyginma.httplogging.reactiveweb.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class HttpLogger {

    public void logRequest(ServerWebExchange exchange, AtomicReference<String> requestBodyRef) {
        log.info("Request Method: {}", exchange.getRequest().getMethod());
        log.info("Request URL: {}", exchange.getRequest().getURI());
        exchange.getRequest().getHeaders().forEach((name, values) ->
                values.forEach(value -> log.info("Request Header: {}: {}", name, value))
        );

        if (requestBodyRef.get() != null && !requestBodyRef.get().isEmpty()) {
            log.info("Request Body: {}", requestBodyRef.get());
        }
    }

    public void logResponse(ServerWebExchange exchange, AtomicReference<String> responseBodyRef, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        log.info("Response Status Code: {}", exchange.getResponse().getStatusCode());
        exchange.getResponse().getHeaders().forEach((name, values) ->
                values.forEach(value -> log.info("Response Header: {}: {}", name, value))
        );
        log.info("Response Duration: {} ms", duration);

        if (responseBodyRef.get() != null && !responseBodyRef.get().isEmpty()) {
            log.info("Response Body: {}", responseBodyRef.get());
        }
    }
}
